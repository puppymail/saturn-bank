package com.saturn_bank.operator.controller;

import com.saturn_bank.operator.dto.UserDto;
import com.saturn_bank.operator.dto.UserMapper;
import com.saturn_bank.operator.service.user.UserAuthService;
import com.saturn_bank.operator.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;
    private final UserAuthService userAuthService;
    private final PasswordEncoder encoder;
    private final UserMapper mapper;

    @Autowired
    public AuthController(UserService userService,
                          UserAuthService userAuthService,
                          PasswordEncoder encoder,
                          UserMapper mapper) {
        this.userService = userService;
        this.userAuthService = userAuthService;
        this.encoder = encoder;
        this.mapper = mapper;
    }

    @GetMapping("/register")
    public String showRegister(Model model) {

        model.addAttribute("bankUser", new UserDto());

        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserDto userDto) {

        userDto.setPassword(encoder.encode(userDto.getPassword()));
        userService.createUser(mapper.dtoToUser(userDto));

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "auth/login";
    }

}
