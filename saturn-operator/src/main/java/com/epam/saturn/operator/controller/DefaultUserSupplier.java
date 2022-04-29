package com.epam.saturn.operator.controller;

import static java.time.LocalDate.parse;
import static java.time.LocalDateTime.now;
import static java.util.Objects.requireNonNull;

import com.epam.saturn.operator.dao.User;
import com.epam.saturn.operator.dao.UserRole;
import com.epam.saturn.operator.dao.UserType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.function.Supplier;

@Slf4j
@Component
@PropertySource("classpath:defaultUserData.properties")
public class DefaultUserSupplier implements Supplier<User> {

    private static final String FIRST_NAME_PROP_NAME = "firstName";
    private static final String LAST_NAME_PROP_NAME = "lastName";
    private static final String MIDDLE_NAME_PROP_NAME = "middleName";
    private static final String PHONE_NUMBER_PROP_NAME = "phoneNumber";
    private static final String EMAIL_PROP_NAME = "email";
    private static final String BIRTH_DATE_PROP_NAME = "birthDate";
    private static final String PASSWORD_PROP_NAME = "password";
    private static final String TYPE_PROP_NAME = "type";
    private static final String ROLE_PROP_NAME = "role";

    @Autowired
    Environment env;

    LocalDateTime now;

    @Override
    public User get() {
        now = now();

        return User.builder()
                .firstName(env.getProperty(FIRST_NAME_PROP_NAME))
                .lastName(env.getProperty(LAST_NAME_PROP_NAME))
                .middleName(env.getProperty(MIDDLE_NAME_PROP_NAME))
                .phoneNumber(env.getProperty(PHONE_NUMBER_PROP_NAME))
                .email(env.getProperty(EMAIL_PROP_NAME))
                .birthDate(parse(requireNonNull(env.getProperty(BIRTH_DATE_PROP_NAME))))
                .registrationDate(now)
                .lastModified(now)
                .lastLogin(now)
                .password(env.getProperty(PASSWORD_PROP_NAME))
                .type(UserType.valueOf(env.getProperty(TYPE_PROP_NAME)))
                .role(UserRole.valueOf(env.getProperty(ROLE_PROP_NAME)))
                .build();
    }



}
