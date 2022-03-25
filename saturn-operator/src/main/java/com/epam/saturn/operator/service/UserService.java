package com.epam.saturn.operator.service;

import com.epam.saturn.operator.dao.CardEntityDao;
import com.epam.saturn.operator.dao.User;
import com.epam.saturn.operator.dao.UserRole;
import com.epam.saturn.operator.dao.UserType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService {

    public abstract User createUser(User user);

    public abstract User deleteUser(User user);

    public abstract User editUser(User user);

    public abstract Boolean logIn(User user);

    public abstract Boolean logIn(CardEntityDao card);

    public abstract void logOut(User user);

    public abstract List<User> findAll();

    public abstract Optional<User> findById(Long id);

    public abstract List<User> findByFirstName(String firstName);

    public abstract List<User> findByLastName(String lastName);

    public abstract List<User> findByBirthDate(LocalDate birthDate);

    public abstract List<User> findByType(UserType userType);

    public abstract List<User> findByRole(UserRole userRole);

    public abstract Optional<User> findByPhoneNumber(String phoneNumber);

    public abstract Optional<User> findByEmail(String email);

}
