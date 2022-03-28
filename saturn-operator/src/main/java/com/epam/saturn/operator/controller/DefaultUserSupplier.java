package com.epam.saturn.operator.controller;

import com.epam.saturn.operator.dao.User;
import com.epam.saturn.operator.dao.UserRole;
import com.epam.saturn.operator.dao.UserType;
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
        try (
                InputStream propsFileStream = getClass()
                        .getClassLoader()
                        .getResourceAsStream(DEFAULT_USER_DATA_PROPERTIES)
        ) {
            userTestData.load(propsFileStream);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        user.setFirstName(userTestData.getProperty("firstName"));
        user.setLastName(userTestData.getProperty("lastName"));
        user.setMiddleName(userTestData.getProperty("middleName"));
        user.setPhoneNumber(userTestData.getProperty("phoneNumber"));
        user.setEmail(userTestData.getProperty("email"));
        user.setBirthDate(LocalDate.parse(userTestData.getProperty("dateOfBirth")));
        user.setRegistrationDate(LocalDateTime.now());
        user.setLastLogin(user.getRegistrationDate());
        user.setType(UserType.valueOf(userTestData.getProperty("type")));
        user.setRole(UserRole.valueOf(userTestData.getProperty("role")));

        return user;
    }

}
