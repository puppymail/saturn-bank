package com.epam.saturn.operator.service;

import com.epam.saturn.operator.dao.Card;
import com.epam.saturn.operator.dao.User;
import com.epam.saturn.operator.dao.UserRole;
import com.epam.saturn.operator.dao.UserType;
import com.epam.saturn.operator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        userRepository.save(user);

        return user;
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User editUser(User user, Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Boolean logIn(User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Boolean logIn(Card card) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void logOut(User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findByFirstName(String firstName) {
        return userRepository.findByFirstName(firstName);
    }

    @Override
    public List<User> findByLastName(String lastName) {
        return userRepository.findByLastName(lastName);
    }

    @Override
    public List<User> findByBirthDate(LocalDate birthDate) {
        return userRepository.findByBirthDate(birthDate);
    }

    @Override
    public List<User> findByType(UserType type) {
        return userRepository.findByType(type);
    }

    @Override
    public List<User> findByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    @Override
    public List<User> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public List<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
