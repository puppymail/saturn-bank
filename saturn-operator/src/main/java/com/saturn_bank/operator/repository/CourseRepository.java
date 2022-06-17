package com.saturn_bank.operator.repository;

import com.saturn_bank.operator.dao.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByNumCode(String numCode);


    @Transactional
    @Modifying
    @Query(value = "TRUNCATE saturn_bank.course RESTART IDENTITY", nativeQuery = true)
    void truncateAndRestartIdentity();
}
