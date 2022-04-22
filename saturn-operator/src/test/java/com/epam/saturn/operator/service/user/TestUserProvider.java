package com.epam.saturn.operator.service.user;

import static java.time.LocalDateTime.now;

import com.epam.saturn.operator.dao.User;
import com.epam.saturn.operator.dao.UserRole;
import com.epam.saturn.operator.dao.UserType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Properties;

@Component
public class TestUserProvider {

    private final static String TEST_USER_DATA_PROPERTIES = "testUserData.properties";
    private final static String DEFAULT_PASSWORD = "admin";
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
        LocalDateTime now = now();

        return User.builder()
                .id(testUserId + 1L)
                .firstName(userTestData.getProperty(testUserId + ".firstName"))
                .lastName(userTestData.getProperty(testUserId + ".lastName"))
                .middleName(userTestData.getProperty(testUserId + ".middleName"))
                .phoneNumber(userTestData.getProperty(testUserId + ".phoneNumber"))
                .email(userTestData.getProperty(testUserId + ".email"))
                .birthDate(LocalDate.parse(userTestData.getProperty(testUserId + ".dateOfBirth")))
                .registrationDate(now)
                .lastModified(now)
                .lastLogin(now)
                .type(UserType.valueOf(userTestData.getProperty(testUserId + ".type")))
                .role(UserRole.valueOf(userTestData.getProperty(testUserId + ".role")))
                .build();
    }

}
