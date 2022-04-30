package com.saturn_bank.operator.service.user;

import com.saturn_bank.operator.dao.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserAuthService extends UserDetailsService {

    void changePassword(User user, String rawPassword);

    void changePassword(Long id, String rawPassword);

}
