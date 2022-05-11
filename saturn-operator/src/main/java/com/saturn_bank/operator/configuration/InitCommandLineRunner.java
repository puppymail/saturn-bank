package com.saturn_bank.operator.configuration;

import static com.saturn_bank.operator.controller.DefaultUserSupplier.BIRTH_DATE_PROP_NAME;
import static com.saturn_bank.operator.controller.DefaultUserSupplier.EMAIL_PROP_NAME;
import static com.saturn_bank.operator.controller.DefaultUserSupplier.FIRST_NAME_PROP_NAME;
import static com.saturn_bank.operator.controller.DefaultUserSupplier.LAST_NAME_PROP_NAME;
import static com.saturn_bank.operator.controller.DefaultUserSupplier.MIDDLE_NAME_PROP_NAME;
import static com.saturn_bank.operator.controller.DefaultUserSupplier.PASSWORD_PROP_NAME;
import static com.saturn_bank.operator.controller.DefaultUserSupplier.PHONE_NUMBER_PROP_NAME;
import static com.saturn_bank.operator.controller.DefaultUserSupplier.REG_DATE_PROP_NAME;
import static com.saturn_bank.operator.controller.DefaultUserSupplier.ROLE_PROP_NAME;
import static java.time.LocalDate.parse;
import static java.time.LocalDateTime.now;
import static java.util.Objects.requireNonNull;

import com.saturn_bank.operator.dao.User;
import com.saturn_bank.operator.dao.UserRole;
import com.saturn_bank.operator.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Slf4j
@Component
@PropertySource("classpath:adminData.properties")
public class InitCommandLineRunner implements CommandLineRunner {

    private static final String ADMIN_PREFIX = "admin.";

    UserRepository userRepo;
    Environment env;
    PasswordEncoder encoder;

    @Autowired
    public InitCommandLineRunner(UserRepository userRepo, Environment env, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.env = env;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepo.findByPhoneNumber(requireNonNull(env.getProperty(ADMIN_PREFIX + PHONE_NUMBER_PROP_NAME))).isEmpty()) {
            LocalDateTime now = now();
            User user = User.builder()
                    .firstName(requireNonNull(env.getProperty(ADMIN_PREFIX + FIRST_NAME_PROP_NAME)))
                    .lastName(requireNonNull(env.getProperty(ADMIN_PREFIX + LAST_NAME_PROP_NAME)))
                    .middleName(env.getProperty(ADMIN_PREFIX + MIDDLE_NAME_PROP_NAME))
                    .phoneNumber(requireNonNull(env.getProperty(ADMIN_PREFIX + PHONE_NUMBER_PROP_NAME)))
                    .email(requireNonNull(env.getProperty(ADMIN_PREFIX + EMAIL_PROP_NAME)))
                    .birthDate(parse(requireNonNull(env.getProperty(ADMIN_PREFIX + BIRTH_DATE_PROP_NAME))))
                    .registrationDate(now)
                    .lastModified(now)
                    .password(encoder.encode(requireNonNull(env.getProperty(ADMIN_PREFIX + PASSWORD_PROP_NAME))))
                    .role(UserRole.valueOf(requireNonNull(env.getProperty(ADMIN_PREFIX + ROLE_PROP_NAME))))
                    .build();
            long id = userRepo.save(user).getId();
            log.debug("Admin user with id=" + id + " saved.");
            log.debug("Login=" + env.getProperty(ADMIN_PREFIX + PHONE_NUMBER_PROP_NAME));
            log.debug("Pass=" + env.getProperty(ADMIN_PREFIX + PASSWORD_PROP_NAME));
        }
    }

}
