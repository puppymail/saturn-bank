package com.epam.saturn.operator.service;

import com.epam.saturn.operator.dao.CardEntityDao;
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
    public User deleteUser(User user) {
        userRepository.delete(user);

        return user;
    }

    @Override
    public User editUser(User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Boolean logIn(User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Boolean logIn(CardEntityDao card) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void logOut(User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<User> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<User> findById(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<User> findByFirstName(String firstName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<User> findByLastName(String lastName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<User> findByBirthDate(LocalDate birthDate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<User> findByType(UserType userType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<User> findByRole(UserRole userRole) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        throw new UnsupportedOperationException();
    }

}
