package com.saturn_bank.operator.repository;

import com.saturn_bank.operator.dao.User;

import com.saturn_bank.operator.dao.UserRole;
import com.saturn_bank.operator.dao.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, SoftDeleteEntityRepository<User, Long> {

    List<User> findByFirstName(String firstName);

    List<User> findByLastName(String lastName);

    List<User> findByBirthDate(LocalDate birthDate);

    List<User> findByRoleIsIn(Collection<UserRole> roles);

    List<User> findByRole(UserRole role);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByEmail(String email);

}
