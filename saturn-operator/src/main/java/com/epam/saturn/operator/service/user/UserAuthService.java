package com.epam.saturn.operator.service.user;

import com.epam.saturn.operator.dao.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserAuthService extends UserDetailsService {

    void changePassword(User user, String rawPassword);

    void changePassword(Long id, String rawPassword);

}
