package com.epam.saturn.operator.service.user;

import com.epam.saturn.operator.dao.Card;
import com.epam.saturn.operator.dao.User;
import com.epam.saturn.operator.dao.UserRole;
import com.epam.saturn.operator.dao.UserType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService {

    User createUser(User user);

    void deleteUser(User user);

    void deleteUser(Long id);

    void editUser(User newUser, Long id);

    void editUser(User newUser, User oldUser);

    Boolean logIn(User user);

    Boolean logIn(Card card);

    void logOut(User user);

    List<User> findAll();

    List<User> findAll(Boolean isDeleted);

    List<User> findAllByExample(User user);

    Optional<User> findByExample(User user);

    Optional<User> findById(Long id);

    List<User> findByFirstName(String firstName);

    List<User> findByLastName(String lastName);

    List<User> findByBirthDate(LocalDate birthDate);

    List<User> findByType(UserType type);

    List<User> findByRole(UserRole role);

    Optional<User> findByPhoneNumber(String phoneNumber);

    List<User> findByEmail(String email);

}
