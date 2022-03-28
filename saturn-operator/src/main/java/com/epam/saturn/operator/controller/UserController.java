package com.epam.saturn.operator.controller;

import com.epam.saturn.operator.dao.Account;
import com.epam.saturn.operator.dao.User;
import com.epam.saturn.operator.repository.AccountRepository;

import com.epam.saturn.operator.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AccountRepository accountRepo;

    private final Supplier<User> defaultUserSupplier;

    @Autowired
    public UserController(UserService userService,
                          AccountRepository accountRepo,
                          Supplier<User> defaultUserSupplier) {
        this.userService = userService;
        this.accountRepo = accountRepo;
        this.defaultUserSupplier = defaultUserSupplier;
    }

    @GetMapping()
    public String showUsers(Model model) {
        log.info("Method \"users(Model)\" invoked");
        log.info("-----------------------------");
        List<User> users = userService.findAll();
        model.addAttribute("bankUsers", users);
        log.info("Displaying all users.");

        return "user/users";
    }

    @GetMapping("/{id}")
    public String showUser(@PathVariable("id") long id, Model model) {
        log.info("Method \"showUser(int, Model)\" invoked");
        log.info("---------------------------------");
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isEmpty()) {
            model.addAttribute("bankUser", new User());
            log.error("!Added empty user to the model!");
        } else {
            model.addAttribute("bankUser", userOpt.get());
            log.info("Added user to model.");
        }

        return "user/user";
    }

    @GetMapping("/add-default-user")
    public String addDefaultUser() {
        log.info("Method \"addDefaultUser()\" invoked");
        log.info("---------------------------------");
        User user = defaultUserSupplier.get();
        userService.createUser(user);

        return "redirect:/users";
    }

    @GetMapping("/add-user")
    public String showAddUser(Model model) {
        log.info("Method \"showAddUser(Model)\" invoked");
        log.info("------------------------------");
        model.addAttribute("bankUser", new User());

        return "user/addUser";
    }

    @PostMapping()
    public String addUser(@ModelAttribute("bankUser") User bankUser) {
        log.info("Method \"addUser(User)\" invoked");
        log.info("------------------------------");
        userService.createUser(bankUser);

        return "redirect:/users";
    }

    @GetMapping("/add-default-account")
    public String addDefaultAccount() {
        log.info("Method \"addDefaultAccount()\" invoked");
        log.info("------------------------------------");
        Account account = new Account();
        account.setUser(userService.findAll()
                .stream()
                .findAny()
                .orElseGet(() -> {
            User user = defaultUserSupplier.get();
            log.warn("Couldn't find existing user, inserting new default user in \"bank_user\" table!");
            userService.createUser(user);
            return user;
        }));

        accountRepo.save(account);
        log.info("Default account inserted in \"account\" table belonging to user id=" + account.getUser().getId());

        return "redirect:/users";
    }

    @GetMapping("/clear-all")
    public String clearAll() {
        log.info("Method \"clearAll()\" invoked");
        log.info("---------------------------");
        accountRepo.deleteAll();
        log.info("Cleared all records from \"account\" table.");
        userService.findAll()
                .forEach(userService::deleteUser);
        log.info("Cleared all records from \"bank_user\" table.");

        return "redirect:/users";
    }

}
