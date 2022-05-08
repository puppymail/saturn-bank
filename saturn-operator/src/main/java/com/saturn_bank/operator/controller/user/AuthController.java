package com.saturn_bank.operator.controller.user;

import com.saturn_bank.operator.dao.User;
import com.saturn_bank.operator.dto.UserDto;
import com.saturn_bank.operator.dto.mapper.UserMapper;
import com.saturn_bank.operator.dto.web.LoginForm;
import com.saturn_bank.operator.dto.web.RegistrationForm;
import com.saturn_bank.operator.exception.EntityAlreadyPresentException;
import com.saturn_bank.operator.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

import static com.saturn_bank.operator.controller.Urls.LOGIN_URL;
import static com.saturn_bank.operator.controller.Urls.REDIRECT;
import static com.saturn_bank.operator.controller.Urls.REGISTER_URL;

@Slf4j
@Controller
public class AuthController {

    private static final String AUTH_NAMESPACE = "auth";
    private static final String REGISTER_PAGE = AUTH_NAMESPACE + "/register";
    private static final String LOGIN_PAGE = AUTH_NAMESPACE + "/login";

    private static final String REG_FORM_ATTR_NAME = "registrationForm";
    private static final String LOGIN_FORM_ATTR_NAME = "loginForm";
    private final UserService userService;
    private final UserMapper mapper;

    @Autowired
    public AuthController(UserService userService,
                          UserMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @GetMapping(REGISTER_URL)
    public String showRegister(Model model) {
        model.addAttribute(REG_FORM_ATTR_NAME, new RegistrationForm());

        return REGISTER_PAGE;
    }

    @PostMapping(REGISTER_URL)
    public String register(@ModelAttribute @Valid RegistrationForm form, BindingResult br) {
        if (br.hasErrors()) {
            log.debug(br.getAllErrors().toString());
            return REGISTER_PAGE;
        }
        User user = mapper.registrationFormToUser(form);
        try {
            userService.createUser(user);
        } catch (EntityAlreadyPresentException e) {
            throw new RuntimeException(e);
        }

        return REDIRECT + LOGIN_URL;
    }

    @GetMapping(LOGIN_URL)
    public String showLogin(Model model) {
        model.addAttribute(new LoginForm());

        return LOGIN_PAGE;
    }

}
