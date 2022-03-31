package com.epam.saturn.operator.service;

import com.epam.saturn.operator.dao.Card;
import com.epam.saturn.operator.dao.User;
import com.epam.saturn.operator.dao.UserRole;
import com.epam.saturn.operator.dao.UserType;
import com.epam.saturn.operator.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

// TODO write tests
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
        log.info("Method \"createUser(User)\" invoked");
        log.info("---------------------------------");
        if (user.getId() != null && userRepository.existsById(user.getId())) {
            log.error("!User with provided id=" + user.getId() + " already exists!");
            throw new IllegalArgumentException("User with provided id=" + user.getId() + " already exists");
        }
        user.setRegistrationDate(LocalDateTime.now());
        log.info("User created at: " + user.getRegistrationDate());

        user.setLastLogin(user.getRegistrationDate());
        user.setLastModified(user.getRegistrationDate());
        user.setIsDeleted(false);

        userRepository.save(user);
        log.info("New user saved.");

        return user;
    }

    @Transactional
    @Override
    public void deleteUser(User user) {
        log.info("Method \"deleteUser(User)\" invoked");
        log.info("---------------------------------");
        Optional<User> existingUser;
        if ( ( existingUser = userRepository.findOne(Example.of(user)) ).isEmpty() ) {
            log.error("!No such user found!");
            return;
        }
        userRepository.delete(existingUser.get());
        log.info("User with id " + user.getId() + " set as deleted.");
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        log.info("Method \"deleteUser(Long)\" invoked");
        log.info("---------------------------------");
        Optional<User> existingUser;
        if ( ( existingUser = userRepository.findById(id) ).isEmpty() ) {
            log.error("!No user found with id=" + id + "!");
            return;
        }
        User user = existingUser.get();
        userRepository.delete(user);
        log.info("User with id " + user.getId() + " set as deleted.");
    }

    @Transactional
    @Override
    public void editUser(User updatedUser, User existingUser) {
        log.info("Method \"editUser(User, User)\" invoked");
        log.info("-------------------------------------");
        if (Objects.isNull(updatedUser)) {
            log.error("!newUser provided is null!");
            throw new NullPointerException("newUser provided is null");
        }
        if (Objects.isNull(existingUser)) {
            log.error("!oldUser provided is null!");
            throw new NullPointerException("oldUser provided is null");
        }
        Optional<User> existingUserOpt;
        if ( ( existingUserOpt = userRepository.findOne(Example.of(existingUser)) ).isEmpty() ) {
            log.error("!No such user found!");
            throw new IllegalArgumentException("No such user found");
        }
        if (existingUserOpt.get().getIsDeleted()) {
            log.warn("!User being edited is marked as deleted!");
        }
        updateUser(updatedUser, existingUserOpt.get());
    }

    @Transactional
    @Override
    public void editUser(User updatedUser, Long id) {
        log.info("Method \"editUser(User, Long)\" invoked");
        log.info("-------------------------------------");
        if (Objects.isNull(updatedUser)) {
            log.error("!newUser provided is null!");
            throw new NullPointerException("User provided is null");
        }
        Optional<User> existingUser;
        if ( ( existingUser = userRepository.findById(id) ).isEmpty() ) {
            log.error("!No user found with id=" + id + "!");
            throw new IllegalArgumentException("No user found with id=" + id);
        }
        if (existingUser.get().getIsDeleted()) {
            log.warn("User to be updated is marked as deleted!");
        }
        updateUser(updatedUser, existingUser.get());
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
        log.info("Method \"findAll()\" invoked");
        log.info("--------------------------");
        List<User> users = userRepository.findAll();
        log.info("Fetching list of all users.");

        return users;
    }

    @Override
    public List<User> findAll(Boolean isDeleted) {
        log.info("Method \"findAll(Boolean)\" invoked");
        log.info("---------------------------------");
        if (Objects.isNull(isDeleted)) {
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
        log.info("Method \"findAllByExample(User)\" invoked");
        log.info("---------------------------------------");
        if (Objects.isNull(user)) {
            log.warn("User provided is null, returning empty list!");
            return List.of();
        }
        List<User> users = userRepository.findAll(Example.of(user));
        log.info("Returning list of matching users.");

        return users;
    }

    @Override
    public Optional<User> findByExample(User user) {
        log.info("Method \"findByExample(User)\" invoked");
        log.info("------------------------------------");
        if (Objects.isNull(user)) {
            log.warn("User provided is null, returning empty optional!");
            return Optional.empty();
        }
        Optional<User> foundUser = userRepository.findOne(Example.of(user));
        log.info("Returning optional of matching user.");

        return foundUser;
    }

    @Override
    public Optional<User> findById(Long id) {
        log.info("Method \"findById(Long)\" invoked");

        return userRepository.findById(id);
    }

    @Override
    public List<User> findByFirstName(String firstName) {
        log.info("Method \"findByFirstName(String)\" invoked");

        return userRepository.findByFirstName(firstName);
    }

    @Override
    public List<User> findByLastName(String lastName) {
        log.info("Method \"findByLastName(String)\" invoked");

        return userRepository.findByLastName(lastName);
    }

    @Override
    public List<User> findByBirthDate(LocalDate birthDate) {
        log.info("Method \"findByBirthDate(LocalDate)\" invoked");

        return userRepository.findByBirthDate(birthDate);
    }

    @Override
    public List<User> findByType(UserType type) {
        log.info("Method \"findByType(UserType)\" invoked");

        return userRepository.findByType(type);
    }

    @Override
    public List<User> findByRole(UserRole role) {
        log.info("Method \"findByRole(UserRole)\" invoked");

        return userRepository.findByRole(role);
    }

    @Override
    public List<User> findByPhoneNumber(String phoneNumber) {
        log.info("Method \"findByPhoneNumber(String)\" invoked");

        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public List<User> findByEmail(String email) {
        log.info("Method \"findByEmail(String)\" invoked");

        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findByCriterion(String criterion, String value) {
        log.info("Method \"findByCriterion(String, String)\" invoked");
        log.info("------------------------------------------------");
        switch (criterion) {
            case "firstName":
                log.info("Criterion is \"first name\".");
                return findByFirstName(value);
            case "lastName":
                log.info("Criterion is \"last name\".");
                return findByLastName(value);
            case "birthDate":
                log.info("Criterion is \"birth date\".");
                return findByBirthDate(LocalDate.parse(value));
            case "type":
                log.info("Criterion is \"type\".");
                return findByType(UserType.valueOf(value));
            case "role":
                log.info("Criterion is \"role\".");
                return findByRole(UserRole.valueOf(value));
            case "phoneNumber":
                log.info("Criterion is \"phone number\".");
                return findByPhoneNumber(value);
            case "email":
                log.info("Criterion is \"email\".");
                return findByEmail(value);
            default:
                log.error("!Wrong criterion, returning empty list!");
                return List.of();
        }
    }

    // TODO refactor
    private void updateUser(User updatedUser, User existingUser) {
        if (Objects.nonNull(updatedUser.getFirstName())) {
            existingUser.setFirstName(updatedUser.getFirstName());
        }
        if (Objects.nonNull(updatedUser.getMiddleName())) {
            existingUser.setMiddleName(updatedUser.getMiddleName());
        }
        if (Objects.nonNull(updatedUser.getLastName())) {
            existingUser.setLastName(updatedUser.getLastName());
        }
        if (Objects.nonNull(updatedUser.getBirthDate())) {
            existingUser.setBirthDate(updatedUser.getBirthDate());
        }
        if (Objects.nonNull(updatedUser.getPhoneNumber())) {
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        }
        if (Objects.nonNull(updatedUser.getEmail())) {
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (Objects.nonNull(updatedUser.getType())) {
            existingUser.setType(updatedUser.getType());
        }
        if (Objects.nonNull(updatedUser.getRole())) {
            existingUser.setRole(updatedUser.getRole());
        }
        existingUser.setLastModified(LocalDateTime.now());
        userRepository.save(existingUser);
        log.info("User with id=" + existingUser.getId() + " updated.");
    }

}
