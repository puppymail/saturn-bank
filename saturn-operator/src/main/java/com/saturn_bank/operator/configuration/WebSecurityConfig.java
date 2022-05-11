package com.saturn_bank.operator.configuration;

import static com.saturn_bank.operator.controller.Uris.LOGIN_URI;
import static com.saturn_bank.operator.controller.Uris.Q_MARK;
import static com.saturn_bank.operator.controller.Uris.REGISTER_URI;
import static com.saturn_bank.operator.controller.Uris.SLASH;

import com.saturn_bank.operator.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    UserService userService;

    PasswordEncoder encoder;

    @Autowired
    public WebSecurityConfig(UserService userService, PasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(encoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(SLASH, REGISTER_URI, LOGIN_URI)
                .permitAll()
//                .antMatchers(LOGIN_URL, REGISTER_URL)
//                .not().authenticated()
                .anyRequest()
                .hasRole("STAFF")
                .and()
                .formLogin()
                .loginPage(LOGIN_URI)
                .loginProcessingUrl(LOGIN_URI)
                .defaultSuccessUrl(SLASH, true)
                .failureUrl(LOGIN_URI + Q_MARK + "error=true")
                .usernameParameter("login")
                .passwordParameter("password")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl(LOGIN_URI + Q_MARK + "logout=true")
                .permitAll();
    }

}
