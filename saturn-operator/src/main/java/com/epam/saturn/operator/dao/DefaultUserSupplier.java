package com.epam.saturn.operator.dao;

import org.springframework.stereotype.Component;

import java.io.IOException;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.Properties;
import java.util.function.Supplier;

@Component
public class DefaultUserSupplier implements Supplier<User> {

    private static final String DEFAULT_USER_DATA_PROPERTIES = "defaultUserData.properties";

    @Override
    public User get() {
        User user = new User();
        Properties userTestData = new Properties();
        try (InputStream propsFileStream = getClass().getClassLoader()
                                                     .getResourceAsStream(DEFAULT_USER_DATA_PROPERTIES)) {
            userTestData.load(propsFileStream);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        user.setFullName(userTestData.getProperty("fullName"));
        user.setPhoneNumber(userTestData.getProperty("phoneNumber"));
        user.setBirthDate(LocalDate.parse(userTestData.getProperty("dateOfBirth")));
        user.setRegistrationDate(LocalDateTime.now());
        user.setType(UserType.valueOf(userTestData.getProperty("type")));
        user.setRole(UserRole.valueOf(userTestData.getProperty("role")));

        return user;
    }

}
