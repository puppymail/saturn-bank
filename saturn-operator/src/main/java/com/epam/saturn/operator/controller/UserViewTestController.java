package com.epam.saturn.operator.controller;

import com.epam.saturn.operator.dao.Account;
import com.epam.saturn.operator.dao.DefaultUserSupplier;
import com.epam.saturn.operator.dao.User;
import com.epam.saturn.operator.repository.AccountRepository;
import com.epam.saturn.operator.repository.UserRepository;

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

    private final UserRepository userRepo;
    private final AccountRepository accountRepo;

    private final Supplier<User> userSupplier;

    @Autowired
    public UserViewTestController(UserRepository userRepo, AccountRepository accountRepo, Supplier<User> userSupplier) {
        this.userRepo = userRepo;
        this.accountRepo = accountRepo;
        this.userSupplier = userSupplier;
    }

    @GetMapping("/")
    public String testView(Model model) {
        List<User> users = userRepo.findAll();
        model.addAttribute("users", users);

        log.info("displaying all users");

        return "userViewTest/users";
    }

    @GetMapping("/add-user")
    public String addTestUser() {
        User user = userSupplier.get();
        userRepo.save(user);

        log.info("added new user: " + user);

        return "redirect:/users/";
    }

    @GetMapping("/add-account")
    public String addTestAccount() {
        Account account = new Account();

        account.setUser(userRepo.findAll()
                                .stream()
                                .findAny()
                                .orElseGet(() -> {
            User user = userSupplier.get();
            log.info("couldn't find existing user, inserting new default user in \"bank_user\" table");
            userRepo.save(user);
            return user;
        }));

        accountRepo.save(account);
        log.info("added new account: " + account + " to user: " + account.getUser());

        return "redirect:/users/";
    }

    @GetMapping("/clear-all")
    public String clearAll() {
        accountRepo.deleteAll();
        userRepo.deleteAll();

        log.info("cleared all records from \"bank_user\" and \"account\" tables");

        return "redirect:/users/";
    }

}
