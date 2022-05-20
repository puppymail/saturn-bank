package com.saturn_bank.operator.configuration;

import static com.saturn_bank.operator.configuration.PropertiesConfig.ADMIN_DATA_PROPS_FILE;
import static com.saturn_bank.operator.configuration.PropertiesConfig.ADMIN_NAMESPACE;
import static com.saturn_bank.operator.configuration.PropertiesConfig.CLASSPATH;
import static com.saturn_bank.operator.configuration.PropertiesConfig.USER_BIRTH_PROP;
import static com.saturn_bank.operator.configuration.PropertiesConfig.USER_EMAIL_PROP;
import static com.saturn_bank.operator.configuration.PropertiesConfig.USER_FIRST_N_PROP;
import static com.saturn_bank.operator.configuration.PropertiesConfig.USER_LAST_N_PROP;
import static com.saturn_bank.operator.configuration.PropertiesConfig.USER_MIDDLE_N_PROP;
import static com.saturn_bank.operator.configuration.PropertiesConfig.USER_PASS_PROP;
import static com.saturn_bank.operator.configuration.PropertiesConfig.USER_PHONE_PROP;
import static com.saturn_bank.operator.configuration.PropertiesConfig.USER_ROLE_PROP;
import static java.time.LocalDate.parse;
import static java.time.LocalDateTime.now;
import static java.util.Objects.requireNonNull;

import com.saturn_bank.operator.dao.User;
import com.saturn_bank.operator.dao.UserRole;
import com.saturn_bank.operator.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Slf4j
@Component
@Profile("!test")
@PropertySource(CLASSPATH + ADMIN_DATA_PROPS_FILE)
public class InitCommandLineRunner implements CommandLineRunner {

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
        if (userRepo.findByPhoneNumber(requireNonNull(env.getProperty(ADMIN_NAMESPACE + USER_PHONE_PROP))).isEmpty()) {
            LocalDateTime now = now();
            User user = User.builder()
                    .firstName(requireNonNull(env.getProperty(ADMIN_NAMESPACE + USER_FIRST_N_PROP)))
                    .lastName(requireNonNull(env.getProperty(ADMIN_NAMESPACE + USER_LAST_N_PROP)))
                    .middleName(env.getProperty(ADMIN_NAMESPACE + USER_MIDDLE_N_PROP))
                    .phoneNumber(requireNonNull(env.getProperty(ADMIN_NAMESPACE + USER_PHONE_PROP)))
                    .email(requireNonNull(env.getProperty(ADMIN_NAMESPACE + USER_EMAIL_PROP)))
                    .birthDate(parse(requireNonNull(env.getProperty(ADMIN_NAMESPACE + USER_BIRTH_PROP))))
                    .registrationDate(now)
                    .lastModified(now)
                    .password(encoder.encode(requireNonNull(env.getProperty(ADMIN_NAMESPACE + USER_PASS_PROP))))
                    .role(UserRole.valueOf(requireNonNull(env.getProperty(ADMIN_NAMESPACE + USER_ROLE_PROP))))
                    .build();
            long id = userRepo.save(user).getId();
            log.debug("Admin user with id=" + id + " saved.");
            log.debug("Login=" + env.getProperty(ADMIN_NAMESPACE + USER_PHONE_PROP));
            log.debug("Pass=" + env.getProperty(ADMIN_NAMESPACE + USER_PASS_PROP));
        }
    }

}
