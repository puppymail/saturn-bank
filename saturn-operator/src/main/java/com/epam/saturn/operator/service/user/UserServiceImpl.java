package com.epam.saturn.operator.service.user;

import static java.time.LocalDateTime.now;
import static java.util.Objects.nonNull;
import static java.util.Objects.isNull;
import static java.time.LocalDateTime.now;

import com.epam.saturn.operator.dao.Card;
import com.epam.saturn.operator.dao.User;
import com.epam.saturn.operator.dao.UserRole;
import com.epam.saturn.operator.dao.UserType;
import com.epam.saturn.operator.repository.UserRepository;
import com.epam.saturn.operator.service.exceptions.EntityAlreadyPresentException;
import com.epam.saturn.operator.service.exceptions.NoSuchEntityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public User createUser(User user) {
        if (nonNull(user.getId()) && userRepository.existsById(user.getId())) {
            log.error("!User with provided id=" + user.getId() + " already exists!");
            throw new EntityAlreadyPresentException("User with provided id=" + user.getId() + " already exists");
        }
        user.setRegistrationDate(now());
        log.info("User created at: " + user.getRegistrationDate());

        user.setLastLogin(user.getRegistrationDate());
        user.setLastModified(user.getRegistrationDate());
        user.setDeleted(Boolean.FALSE);

        userRepository.save(user);

        return user;
    }

    @Transactional
    @Override
    public void deleteUser(User user) {
        Optional<User> existingUser;
        if ( ( existingUser = userRepository.findOne(Example.of(user)) ).isEmpty() ) {
            log.error("!No such user found!");
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
    public void editUser(User updatedUser, User existingUser) {
        if (isNull(updatedUser)) {
            log.error("!newUser provided is null!");
            throw new NullPointerException("newUser provided is null");
        }
        if (isNull(existingUser)) {
            log.error("!oldUser provided is null!");
            throw new NullPointerException("oldUser provided is null");
        }
        Optional<User> existingUserOpt;
        if ( ( existingUserOpt = userRepository.findOne(Example.of(existingUser)) ).isEmpty() ) {
            log.error("!No such user found!");
            throw new NoSuchEntityException("No such user found");
        }
        if (existingUserOpt.get().isDeleted()) {
            log.warn("!User being edited is marked as deleted!");
        }
        updateUser(updatedUser, existingUserOpt.get());
    }

    @Override
    public void editUser(User updatedUser, Long id) {
        if (isNull(id) || id <= 0) {
            log.error("!Invalid id provided!");
            throw new IllegalArgumentException("Invalid id provided");
        }
        User existingUser = new User();
        existingUser.setId(id);
        editUser(updatedUser, existingUser);
    }

    // TODO implement
    @Override
    public Boolean logIn(User user) {
        throw new UnsupportedOperationException();
    }

    // TODO implement
    @Override
    public Boolean logIn(Card card) {
        throw new UnsupportedOperationException();
    }

    // TODO implement
    @Override
    public void logOut(User user) {
        throw new UnsupportedOperationException();
    }


    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findAll(Boolean isDeleted) { //TODO log refactor
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

        return userRepository.findByType(type);
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
    public List<User> findByEmail(String email) {
        if (isNull(email) || email.isBlank()) {
            log.warn("Email provided is empty or null, returning empty list!");
            return List.of();
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
        existingUser.setType(getValue(updatedUser.getType(), existingUser.getType()));
        existingUser.setRole(getValue(updatedUser.getRole(), existingUser.getRole()));
        existingUser.setLastModified(now());

        userRepository.save(existingUser);
        log.info("User with id=" + existingUser.getId() + " updated.");
    }

    private <T> T getValue(T newValue, T oldValue) {
        return isNull(newValue) ? oldValue : newValue;
    }

}
