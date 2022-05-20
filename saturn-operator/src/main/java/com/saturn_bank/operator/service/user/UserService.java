package com.saturn_bank.operator.service.user;

import com.saturn_bank.operator.dao.User;
import com.saturn_bank.operator.dao.UserRole;
import com.saturn_bank.operator.dao.UserType;
import com.saturn_bank.operator.exception.EntityAlreadyPresentException;
import com.saturn_bank.operator.exception.NoSuchEntityException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    User createUser(User user) throws EntityAlreadyPresentException;

    void deleteUser(User user) throws NoSuchEntityException;

    void deleteUser(Long id) throws NoSuchEntityException;

    void editUser(User newUser, Long id) throws NoSuchEntityException;

    void editUser(User newUser, User oldUser) throws NoSuchEntityException;

    void changePassword(User user, String rawPassword) throws NoSuchEntityException;

    void changePassword(Long id, String rawPassword) throws NoSuchEntityException;

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

    Optional<User> findByEmail(String email);

}
