package com.epam.saturn.operator.repository;

import com.epam.saturn.operator.dao.User;

import com.epam.saturn.operator.dao.UserRole;
import com.epam.saturn.operator.dao.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, SoftDeleteEntityRepository<User, Long> {

    List<User> findByFirstName(String firstName);

    List<User> findByLastName(String lastName);

    List<User> findByBirthDate(LocalDate birthDate);

    List<User> findByType(UserType type);

    List<User> findByRole(UserRole role);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByEmail(String email);

}
