package com.epam.saturn.operator.service.user;

import static java.lang.Integer.parseInt;
import static java.time.LocalDate.parse;
import static java.time.LocalDateTime.now;
import static java.util.Objects.requireNonNull;

import com.epam.saturn.operator.dao.User;
import com.epam.saturn.operator.dao.UserRole;
import com.epam.saturn.operator.dao.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@PropertySource("classpath:testUserData.properties")
public class TestUserProvider {

    private final Environment env;

    private final String DEFAULT_PASSWORD;
    private final int USERS_COUNT;

    @Autowired
    public TestUserProvider(Environment env) {
        this.env = env;
        USERS_COUNT = parseInt(requireNonNull(env.getProperty("usersCount")));
        DEFAULT_PASSWORD = requireNonNull(env.getProperty("defaultPassword"));
    }

    public int getUsersSize() {
        return USERS_COUNT;
    }

    public User get(int testUserId) {
        if (testUserId < 0 || testUserId >= USERS_COUNT) throw new IllegalArgumentException();
        LocalDateTime now = now();

        return User.builder()
                .id(testUserId + 1L)
                .firstName(env.getProperty(testUserId + ".firstName"))
                .lastName(env.getProperty(testUserId + ".lastName"))
                .middleName(env.getProperty(testUserId + ".middleName"))
                .phoneNumber(env.getProperty(testUserId + ".phoneNumber"))
                .email(env.getProperty(testUserId + ".email"))
                .birthDate(parse(requireNonNull(env.getProperty(testUserId + ".dateOfBirth"))))
                .registrationDate(now)
                .lastModified(now)
                .lastLogin(now)
                .password(DEFAULT_PASSWORD)
                .type(UserType.valueOf(env.getProperty(testUserId + ".type")))
                .role(UserRole.valueOf(env.getProperty(testUserId + ".role")))
                .build();
    }

}
