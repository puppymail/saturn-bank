package com.epam.saturn.operator.repository;

import com.epam.saturn.operator.dao.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
