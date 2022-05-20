package com.saturn_bank.operator.controller;

import static com.saturn_bank.operator.configuration.PropertiesConfig.CLASSPATH;
import static com.saturn_bank.operator.configuration.PropertiesConfig.DEFAULT_USER_DATA_PROPS_FILE;
import static com.saturn_bank.operator.configuration.PropertiesConfig.USER_BIRTH_PROP;
import static com.saturn_bank.operator.configuration.PropertiesConfig.USER_EMAIL_PROP;
import static com.saturn_bank.operator.configuration.PropertiesConfig.USER_FIRST_N_PROP;
import static com.saturn_bank.operator.configuration.PropertiesConfig.USER_LAST_N_PROP;
import static com.saturn_bank.operator.configuration.PropertiesConfig.USER_MIDDLE_N_PROP;
import static com.saturn_bank.operator.configuration.PropertiesConfig.USER_PASS_PROP;
import static com.saturn_bank.operator.configuration.PropertiesConfig.USER_PHONE_PROP;
import static com.saturn_bank.operator.configuration.PropertiesConfig.USER_ROLE_PROP;
import static com.saturn_bank.operator.configuration.PropertiesConfig.USER_NAMESPACE;
import static java.time.LocalDate.parse;
import static java.time.LocalDateTime.now;
import static java.util.Objects.requireNonNull;

import com.saturn_bank.operator.dao.User;
import com.saturn_bank.operator.dao.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.function.Supplier;

@Slf4j
@Component
@PropertySource(CLASSPATH + DEFAULT_USER_DATA_PROPS_FILE)
public class DefaultUserSupplier implements Supplier<User> {

    @Autowired
    Environment env;

    LocalDateTime now;

    @Override
    public User get() {
        now = now();

        return User.builder()
                .firstName(env.getProperty(USER_NAMESPACE + USER_FIRST_N_PROP))
                .lastName(env.getProperty(USER_NAMESPACE + USER_LAST_N_PROP))
                .middleName(env.getProperty(USER_NAMESPACE + USER_MIDDLE_N_PROP))
                .phoneNumber(env.getProperty(USER_NAMESPACE + USER_PHONE_PROP))
                .email(env.getProperty(USER_NAMESPACE + USER_EMAIL_PROP))
                .birthDate(parse(requireNonNull(env.getProperty(USER_NAMESPACE + USER_BIRTH_PROP))))
                .registrationDate(now)
                .lastModified(now)
                .password(env.getProperty(USER_NAMESPACE + USER_PASS_PROP))
                .role(UserRole.valueOf(env.getProperty(USER_NAMESPACE + USER_ROLE_PROP)))
                .build();
    }



}
