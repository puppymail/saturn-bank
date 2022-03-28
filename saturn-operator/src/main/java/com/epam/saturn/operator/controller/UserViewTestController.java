package com.epam.saturn.operator.controller;

import com.epam.saturn.operator.dao.Account;
import com.epam.saturn.operator.dao.User;
import com.epam.saturn.operator.repository.AccountRepository;
import com.epam.saturn.operator.repository.UserRepository;

import com.epam.saturn.operator.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserViewTestController {

    private final UserService userService;
    private final AccountRepository accountRepo;

    private final Supplier<User> userSupplier;

    @Autowired
    public UserViewTestController(UserService userService, AccountRepository accountRepo, Supplier<User> userSupplier) {
        this.userService = userService;
        this.accountRepo = accountRepo;
        this.userSupplier = userSupplier;
    }

    @GetMapping("/")
    public String testView(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);

        log.info("Displaying all users");

        return "userViewTest/users";
    }

    @GetMapping("/add-user")
    public String addTestUser() {
        User user = userSupplier.get();
        userService.createUser(user);

        log.info("Added new user: " + user);

        return "redirect:/users/";
    }

    @GetMapping("/add-account")
    public String addTestAccount() {
        Account account = new Account();

        account.setUser(userService.findAll()
                .stream()
                .findAny()
                .orElseGet(() -> {
            User user = userSupplier.get();
            log.error("Couldn't find existing user, inserting new default user in \"bank_user\" table");
            userService.createUser(user);
            return user;
        }));

        accountRepo.save(account);
        log.info("Added new account: " + account + " to user: " + account.getUser());

        return "redirect:/users/";
    }

    @GetMapping("/clear-all")
    public String clearAll() {
        accountRepo.deleteAll();
        userService.findAll()
                .forEach(userService::deleteUser);

        log.info("Cleared all records from \"bank_user\" and \"account\" tables");

        return "redirect:/users/";
    }

}
