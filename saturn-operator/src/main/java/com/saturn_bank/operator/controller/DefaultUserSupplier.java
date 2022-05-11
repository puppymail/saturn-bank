package com.saturn_bank.operator.controller;

import static java.time.LocalDate.parse;
import static java.time.LocalDateTime.now;
import static java.util.Objects.requireNonNull;

import com.saturn_bank.operator.dao.User;
import com.saturn_bank.operator.dao.UserRole;
import com.saturn_bank.operator.dao.UserType;
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

    private static final String USER_PREFIX = "user.";

    public static final String FIRST_NAME_PROP_NAME = "firstName";
    public static final String LAST_NAME_PROP_NAME = "lastName";
    public static final String MIDDLE_NAME_PROP_NAME = "middleName";
    public static final String PHONE_NUMBER_PROP_NAME = "phoneNumber";
    public static final String EMAIL_PROP_NAME = "email";
    public static final String BIRTH_DATE_PROP_NAME = "birthDate";
    public static final String REG_DATE_PROP_NAME = "registrationDate";
    public static final String LAST_MOD_PROP_NAME = "registrationDate";
    public static final String PASSWORD_PROP_NAME = "password";
    public static final String TYPE_PROP_NAME = "type";
    public static final String ROLE_PROP_NAME = "role";

    @Autowired
    Environment env;

    LocalDateTime now;

    @Override
    public User get() {
        now = now();

        return User.builder()
                .firstName(env.getProperty(USER_PREFIX + FIRST_NAME_PROP_NAME))
                .lastName(env.getProperty(USER_PREFIX + LAST_NAME_PROP_NAME))
                .middleName(env.getProperty(USER_PREFIX + MIDDLE_NAME_PROP_NAME))
                .phoneNumber(env.getProperty(USER_PREFIX + PHONE_NUMBER_PROP_NAME))
                .email(env.getProperty(USER_PREFIX + EMAIL_PROP_NAME))
                .birthDate(parse(requireNonNull(env.getProperty(USER_PREFIX + BIRTH_DATE_PROP_NAME))))
                .registrationDate(now)
                .lastModified(now)
                .password(env.getProperty(USER_PREFIX + PASSWORD_PROP_NAME))
                .role(UserRole.valueOf(env.getProperty(USER_PREFIX + ROLE_PROP_NAME)))
                .build();
    }



}
