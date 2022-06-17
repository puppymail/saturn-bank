package com.saturn_bank.operator.service;

import com.saturn_bank.operator.dao.Course;
import com.saturn_bank.operator.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CurrencyConverter {

    private final CourseRepository courseRepository;

    @Autowired
    public CurrencyConverter(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public BigDecimal convert(String srcCurrencyType, String dstCurrencyType, BigDecimal amount) {
        BigDecimal crossCourse = getCrossCourse(srcCurrencyType, dstCurrencyType);
        return amount.multiply(crossCourse);
    }

    public BigDecimal getCrossCourse(String srcCurrencyType, String dstCurrencyType) {
        Course srcCourse = courseRepository.findByNumCode(srcCurrencyType)
                .orElseThrow(() -> new IllegalArgumentException("No currency found at DB"));
        BigDecimal sourceCourseValue = srcCourse.getValue().divide(BigDecimal.valueOf(srcCourse.getNominal()), RoundingMode.FLOOR);
        Course dstCourse = courseRepository.findByNumCode(dstCurrencyType)
                .orElseThrow(() -> new IllegalArgumentException("No currency found at DB"));
        BigDecimal dstCourseValue = dstCourse.getValue().divide(BigDecimal.valueOf(dstCourse.getNominal()), RoundingMode.FLOOR);
        return sourceCourseValue.divide(dstCourseValue, RoundingMode.FLOOR);
    }
}
