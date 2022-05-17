package com.saturn_bank.operator.service.user;

import static com.saturn_bank.operator.constraints.RegEx.IS_EMAIL_REGEX;
import static com.saturn_bank.operator.exception.ExceptionErrorMessages.ENTITY_ALREADY_PRESENT_EX_MSG;
import static com.saturn_bank.operator.exception.ExceptionErrorMessages.INVALID_ID_PROVIDED_EX_MSG;
import static com.saturn_bank.operator.exception.ExceptionErrorMessages.NO_PASSWORD_SET_EX_MSG;
import static com.saturn_bank.operator.exception.ExceptionErrorMessages.NO_SUCH_ENTITY_EX_MSG;
import static com.saturn_bank.operator.exception.ExceptionErrorMessages.NULL_PTR_EX_MSG;
import static java.time.LocalDateTime.now;
import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

import com.saturn_bank.operator.dao.User;
import com.saturn_bank.operator.dao.UserRole;
import com.saturn_bank.operator.dao.UserType;
import com.saturn_bank.operator.repository.UserRepository;
import com.saturn_bank.operator.exception.EntityAlreadyPresentException;
import com.saturn_bank.operator.exception.NoSuchEntityException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Transactional
    @Override
    public User createUser(User user) throws EntityAlreadyPresentException {
        if (nonNull(user.getId()) && userRepository.existsById(user.getId())) {
            log.error("!User with provided id=" + user.getId() + " already exists!");
            throw new EntityAlreadyPresentException(ENTITY_ALREADY_PRESENT_EX_MSG + user.getId());
        }
        String rawPassword = user.getPassword();
        if (isNull(rawPassword) || rawPassword.isBlank()) {
            log.error("!No password set for provided user!");
            throw new IllegalArgumentException(NO_PASSWORD_SET_EX_MSG);
        }
        user.setPassword(encoder.encode(rawPassword));
        user.setRegistrationDate(now());
        user.setLastModified(user.getRegistrationDate());
        user.setDeleted(Boolean.FALSE);

        try {
            userRepository.save(user);
        } catch (ConstraintViolationException cve) {
            log.error("!Constraint " + cve.getConstraintName() + " is violated, unable to save user!");
            throw new EntityAlreadyPresentException(cve);
        }

        return user;
    }

    @Transactional
    @Override
    public void deleteUser(User user) {
        Optional<User> existingUser;
        if ( ( existingUser = userRepository.findOne(Example.of(user)) ).isEmpty() ) {
            log.error(NO_SUCH_ENTITY_EX_MSG);
            return;
        }
        user = existingUser.get();
        userRepository.delete(user);
        log.info("User with id=" + user.getId() + " set as deleted.");
    }

    @Override
    public void deleteUser(Long id) {
        User user = new User();
        user.setId(id);
        deleteUser(user);
    }

    @Transactional
    @Override
    public void editUser(User updatedUser, User existingUser) throws NoSuchEntityException {
        if (isNull(updatedUser)) {
            log.error("!newUser provided is null!");
            throw new NullPointerException(NULL_PTR_EX_MSG);
        }
        if (isNull(existingUser)) {
            log.error("!oldUser provided is null!");
            throw new NullPointerException(NULL_PTR_EX_MSG);
        }
        Optional<User> existingUserOpt;
        if ( ( existingUserOpt = userRepository.findOne(Example.of(existingUser)) ).isEmpty() ) {
            log.error("!No such user found!");
            throw new NoSuchEntityException(NO_SUCH_ENTITY_EX_MSG);
        }
        if (existingUserOpt.get().isDeleted()) {
            log.warn("!User being edited is marked as deleted!");
        }
        updateUser(updatedUser, existingUserOpt.get());
    }

    @Override
    public void editUser(User updatedUser, Long id) throws NoSuchEntityException {
        if (isNull(id) || id <= 0) {
            log.error("!Invalid id provided!");
            throw new IllegalArgumentException(INVALID_ID_PROVIDED_EX_MSG);
        }
        User existingUser = new User();
        existingUser.setId(id);
        editUser(updatedUser, existingUser);
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> userOpt;
        if (isNull(login) || login.isBlank()) {
            throw new UsernameNotFoundException("Empty login provided");
        }
        if (login.matches(IS_EMAIL_REGEX)) {
            userOpt = userRepository.findByEmail(login);
        }
        else {
            userOpt = userRepository.findByPhoneNumber(login);
        }
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("No user found with login: " + login);
        }

        return userOpt.get();
    }

    @Transactional
    @Override
    public void changePassword(User user, String rawPassword) {
        Optional<User> userOpt;
        if ( ( userOpt = userRepository.findOne(Example.of(user)) ).isEmpty() ) {
            log.error("!No such user found!");
            return;
        }
        User existingUser = userOpt.get();
        existingUser.setPassword(encoder.encode(rawPassword));

        userRepository.save(existingUser);
    }

    @Override
    public void changePassword(Long id, String rawPassword) {
        changePassword(User.builder()
                .id(id)
                .build(), rawPassword);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findAll(Boolean isDeleted) {
        if (isNull(isDeleted)) {
            log.warn("Boolean provided is null, setting it to TRUE!");
            isDeleted = Boolean.TRUE;
        }
        List<User> users = userRepository.findByIsDeleted(isDeleted);
        if (isDeleted) {
            log.info("Fetching list of deleted users.");
        } else {
            log.info("Fetching list of active users.");
        }

        return users;
    }

    @Override
    public List<User> findAllByExample(User user) {
        if (isNull(user)) {
            log.warn("User provided is null, returning empty list!");
            return List.of();
        }

        return userRepository.findAll(Example.of(user));
    }

    @Override
    public Optional<User> findByExample(User user) {
        if (isNull(user)) {
            log.warn("User provided is null, returning empty optional!");
            return Optional.empty();
        }

        return userRepository.findOne(Example.of(user));
    }

    @Override
    public Optional<User> findById(Long id) {
        if (isNull(id) || id <= 0) {
            log.warn("Id provided is null or less than 1, returning empty optional!");
            return Optional.empty();
        }

        return userRepository.findById(id);
    }

    @Override
    public List<User> findByFirstName(String firstName) {
        if (isNull(firstName) || firstName.isBlank()) {
            log.warn("First name provided is empty or null, returning empty list!");
            return List.of();
        }

        return userRepository.findByFirstName(firstName);
    }

    @Override
    public List<User> findByLastName(String lastName) {
        if (isNull(lastName) || lastName.isBlank()) {
            log.warn("Last name provided is empty or null, returning empty list!");
            return List.of();
        }

        return userRepository.findByLastName(lastName);
    }

    @Override
    public List<User> findByBirthDate(LocalDate birthDate) {
        if (isNull(birthDate)) {
            log.warn("Birth date provided is null, returning empty list!");
            return List.of();
        }

        return userRepository.findByBirthDate(birthDate);
    }

    @Override
    public List<User> findByType(UserType type) {
        if (isNull(type)) {
            log.warn("Type provided is null, returning empty list!");
            return List.of();
        }

        return userRepository.findByRoleIsIn(stream(UserRole.values())
                .filter(role -> role.getType().equals(type))
                .collect(toList()));
    }

    @Override
    public List<User> findByRole(UserRole role) {
        if (isNull(role)) {
            log.warn("Role provided is null, returning empty list!");
            return List.of();
        }

        return userRepository.findByRole(role);
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        if (isNull(phoneNumber) || phoneNumber.isBlank()) {
            log.warn("Phone number provided is empty or null, returning empty optional!");
            return Optional.empty();
        }

        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if (isNull(email) || email.isBlank()) {
            log.warn("Email provided is empty or null, returning empty list!");
            return Optional.empty();
        }

        return userRepository.findByEmail(email);
    }

    private void updateUser(User updatedUser, User existingUser) {
        existingUser.setFirstName(getValue(updatedUser.getFirstName(), existingUser.getFirstName()));
        existingUser.setMiddleName(getValue(updatedUser.getMiddleName(), existingUser.getMiddleName()));
        existingUser.setLastName(getValue(updatedUser.getLastName(), existingUser.getLastName()));
        existingUser.setBirthDate(getValue(updatedUser.getBirthDate(), existingUser.getBirthDate()));
        existingUser.setPhoneNumber(getValue(updatedUser.getPhoneNumber(), existingUser.getPhoneNumber()));
        existingUser.setEmail(getValue(updatedUser.getEmail(), existingUser.getEmail()));
        existingUser.setRole(getValue(updatedUser.getRole(), existingUser.getRole()));
        existingUser.setLastModified(now());

        userRepository.save(existingUser);
    }

    private <T> T getValue(T newValue, T oldValue) {
        return isNull(newValue) ? oldValue : newValue;
    }

}
