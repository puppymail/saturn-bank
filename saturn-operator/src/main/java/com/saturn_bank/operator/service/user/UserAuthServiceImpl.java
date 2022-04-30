package com.saturn_bank.operator.service.user;

import com.saturn_bank.operator.dao.User;
import com.saturn_bank.operator.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class UserAuthServiceImpl implements UserAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserAuthServiceImpl(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findByPhoneNumber(phoneNumber);
        if (userOpt.isEmpty()) {
            log.error("!No user found with phone number " + phoneNumber + "!");
            throw new UsernameNotFoundException("No user found with phone number " + phoneNumber);
        }

        return userOpt.get();
    }

    @Transactional
    @Override
    public void changePassword(User user, String rawPassword) {
        Optional<User> userOpt;
        if ( ( userOpt = userRepository.findOne(Example.of(user)) ).isEmpty() ) {
            log.error("!No such user found!");
            return;
        }
        User existingUser = userOpt.get();
        existingUser.setPassword(encoder.encode(rawPassword));

        userRepository.save(existingUser);
    }

    @Override
    public void changePassword(Long id, String rawPassword) {
        changePassword(User.builder()
                .id(id)
                .build(), rawPassword);
    }
}
