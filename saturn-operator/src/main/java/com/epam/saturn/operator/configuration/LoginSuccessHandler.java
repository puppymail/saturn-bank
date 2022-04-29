package com.epam.saturn.operator.configuration;

import static java.time.LocalDateTime.now;

import com.epam.saturn.operator.dao.User;
import com.epam.saturn.operator.repository.UserRepository;
import com.epam.saturn.operator.exception.NoSuchEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    UserRepository userRepository;

    @Autowired
    public LoginSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws ServletException, IOException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) authentication.getPrincipal();
        System.out.println(user.getFirstName());
        User currentUser = userRepository.findByPhoneNumber(userDetails.getUsername())
                .orElseThrow(NoSuchEntityException::new);

        currentUser.setLastLogin(now());

        userRepository.save(currentUser);
    }

}
