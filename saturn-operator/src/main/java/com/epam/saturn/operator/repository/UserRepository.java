package com.epam.saturn.operator.repository;

import com.epam.saturn.operator.dao.User;

import com.epam.saturn.operator.dao.UserRole;
import com.epam.saturn.operator.dao.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public abstract List<User> findByFirstName(String firstName);

    public abstract List<User> findByLastName(String lastName);

    public abstract List<User> findByBirthDate(LocalDate birthDate);

    public abstract List<User> findByType(UserType type);

    public abstract List<User> findByRole(UserRole role);

    public abstract List<User> findByPhoneNumber(String phoneNumber);

    public abstract List<User> findByEmail(String email);

}
