package com.saturn_bank.operator.repository;

import com.saturn_bank.operator.dao.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByNumCode(String numCode);
}
