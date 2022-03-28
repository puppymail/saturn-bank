package com.epam.saturn.operator.service;

import com.epam.saturn.operator.dao.Card;
import com.epam.saturn.operator.dao.User;
import com.epam.saturn.operator.dao.UserRole;
import com.epam.saturn.operator.dao.UserType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface UserService {

    public abstract User createUser(User user);

    public abstract void deleteUser(User user);

    public abstract void deleteUser(Long id);

    public abstract void editUser(User newUser, Long id);

    public abstract void editUser(User newUser, User oldUser);

    public abstract Boolean logIn(User user);

    public abstract Boolean logIn(Card card);

    public abstract void logOut(User user);

    public abstract List<User> findAll();

    public abstract Optional<User> findById(Long id);

    public abstract List<User> findByFirstName(String firstName);

    public abstract List<User> findByLastName(String lastName);

    public abstract List<User> findByBirthDate(LocalDate birthDate);

    public abstract List<User> findByType(UserType type);

    public abstract List<User> findByRole(UserRole role);

    public abstract List<User> findByPhoneNumber(String phoneNumber);

    public abstract List<User> findByEmail(String email);

    public abstract List<User> findBy(String criterion, String value);

}
