package com.saturn_bank.operator.service.course;

import com.saturn_bank.operator.dao.Course;
import com.saturn_bank.operator.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@PropertySource("classpath:RUB_Currency_data.properties")
public class CourseService {

    private static final String NUM_CODE = "num_code";
    private static final String CHAR_CODE = "char_code";
    private static final String NOMINAL = "nominal";
    private static final String NAME = "name";
    private static final String VALUE = "value";

    Environment environment;
    private final CourseRepository courseRepository;
    private final CourseClient courseClient;

    @Autowired
    public CourseService(Environment environment, CourseRepository courseRepository, CourseClient courseClient) {
        this.environment = environment;
        this.courseRepository = courseRepository;
        this.courseClient = courseClient;
    }

    @PostConstruct
    public List<Course> populateDBCourseInfo(){
        Course rubCourse = new Course();
        rubCourse.setNumCode(environment.getProperty(NUM_CODE));
        rubCourse.setCharCode(environment.getProperty(CHAR_CODE));
        rubCourse.setNominal(Integer.parseInt(Objects.requireNonNull(environment.getProperty(NOMINAL))));
        rubCourse.setName(environment.getProperty(NAME));
        rubCourse.setValue(BigDecimal.valueOf(Long.parseLong(Objects.requireNonNull(environment.getProperty(VALUE)))));
        rubCourse.set_Value(environment.getProperty(VALUE));
        courseRepository.save(rubCourse);
        return courseRepository.saveAll(courseClient.getCourses());
    }

    @PreDestroy
    public void clearCourses() {
        courseRepository.deleteAll();
    }
}