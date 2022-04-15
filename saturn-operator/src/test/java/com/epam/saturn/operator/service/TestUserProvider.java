package com.epam.saturn.operator.service;

import com.epam.saturn.operator.dao.User;
import com.epam.saturn.operator.dao.UserRole;
import com.epam.saturn.operator.dao.UserType;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Properties;

@Component
public class TestUserProvider {

    private final static String TEST_USER_DATA_PROPERTIES = "testUserData.properties";
    private final int USERS_SIZE;

    Properties userTestData;

    public TestUserProvider() {
        userTestData = new Properties();

        try (
                InputStream propsFileStream = getClass()
                        .getClassLoader()
                        .getResourceAsStream(TEST_USER_DATA_PROPERTIES)
        ) {
            userTestData.load(propsFileStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        USERS_SIZE = Integer.parseInt(userTestData.getProperty("usersSize", "0"));
    }

    public int getUsersSize() {
        return USERS_SIZE;
    }

    public User get(int testUserId) {
        if (testUserId < 0 || testUserId >= USERS_SIZE) throw new IllegalArgumentException();
        LocalDateTime now = LocalDateTime.now();

        User user = new User();
        user.setId(testUserId + 1L);
        user.setFirstName(userTestData.getProperty("firstName" + testUserId));
        user.setLastName(userTestData.getProperty("lastName" + testUserId));
        user.setMiddleName(userTestData.getProperty("middleName" + testUserId));
        user.setPhoneNumber(userTestData.getProperty("phoneNumber" + testUserId));
        user.setEmail(userTestData.getProperty("email" + testUserId));
        user.setBirthDate(LocalDate.parse(userTestData.getProperty("dateOfBirth" + testUserId)));
        user.setRegistrationDate(now);
        user.setLastModified(now);
        user.setLastLogin(now);
        user.setType(UserType.valueOf(userTestData.getProperty("type" + testUserId)));
        user.setRole(UserRole.valueOf(userTestData.getProperty("role" + testUserId)));

        return user;
    }

}
