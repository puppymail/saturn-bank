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
            throw new IllegalArgumentException("!User with provided id=" + user.getId() + " already exists!");
        }
        user.setRegistrationDate(LocalDateTime.now());
        log.info("User created at: " + user.getRegistrationDate());

        user.setLastLogin(user.getRegistrationDate());
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
        setUserAsDeleted(existingUser.get());
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
        setUserAsDeleted(existingUser.get());
    }

    @Transactional
    @Override
    public void editUser(User newUser, User oldUser) {
        log.info("Method \"editUser(User, User)\" invoked");
        log.info("-------------------------------------");
        Optional<User> existingUser;
        if ( ( existingUser = userRepository.findOne(Example.of(oldUser)) ).isEmpty() ) {
            log.error("!No such user found!");
            throw new IllegalArgumentException("!No such user found!");
        }
        if (existingUser.get().getIsDeleted()) log.warn("!User being edited is marked as deleted!");
        updateUser(newUser, existingUser.get());
    }

    @Transactional
    @Override
    public void editUser(User newUser, Long id) {
        log.info("Method \"editUser(User, Long)\" invoked");
        log.info("-------------------------------------");
        Optional<User> existingUser;
        if ( ( existingUser = userRepository.findById(id) ).isEmpty() ) {
            log.error("!No user found with id=" + id + "!");
            throw new IllegalArgumentException("!No user found with id=" + id + "!");
        }
        if (existingUser.get().getIsDeleted()) log.warn("User to be updated is marked as deleted!");
        updateUser(newUser, existingUser.get());
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
        return userRepository.findAll();
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
    public List<User> findBy(String criterion, String value) {
        log.info("Method \"findBy(String, String)\" invoked");
        log.info("---------------------------------------");
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

    // utility method, sets user as deleted
    private void setUserAsDeleted(User user) {
        user.setIsDeleted(true);
        userRepository.save(user);
        log.info("User with id " + user.getId() + " set as deleted.");
    }

    // utility method, updates user
    private void updateUser(User updatedUser, User existingUser) {
        updatedUser.setId(existingUser.getId());
        userRepository.save(updatedUser);
        log.info("User with id=" + existingUser.getId() + " updated.");
    }

}
