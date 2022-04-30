package com.saturn_bank.operator.controller;

import static com.saturn_bank.operator.controller.ControllerConstants.REDIRECT;
import static java.util.stream.Collectors.toList;

import com.saturn_bank.operator.dao.Account;
import com.saturn_bank.operator.dao.User;
import com.saturn_bank.operator.dto.UserDto;
import com.saturn_bank.operator.dto.UserMapper;
import com.saturn_bank.operator.repository.AccountRepository;

import com.saturn_bank.operator.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {

    private static final String USER_NAMESPACE = "user";
    private static final String USERS_PAGE = "/users";
    private static final String USER_PAGE = "/user";
    private static final String EDIT_USER_PAGE = "/editUser";
    private static final String ADD_USER_PAGE = "/addUser";

    private static final String PARAM_SHOW_USERS_ALL = "all";
    private static final String PARAM_SHOW_USERS_DELETED = "deleted";
    private static final String PARAM_SHOW_USERS_ACTIVE = "active";

    private final UserService userService;
    private final AccountRepository accountRepo;

    private final Supplier<User> defaultUserSupplier;
    private final UserMapper mapper;
    private final RandomUserGenerator randomUserGenerator;

    @Autowired
    public UserController(UserService userService,
                          AccountRepository accountRepo,
                          Supplier<User> defaultUserSupplier,
                          UserMapper mapper,
                          RandomUserGenerator randomUserGenerator) {

        this.userService = userService;
        this.accountRepo = accountRepo;
        this.defaultUserSupplier = defaultUserSupplier;
        this.randomUserGenerator = randomUserGenerator;
        this.mapper = mapper;
    }

    @GetMapping()
    public String showUsers(@RequestParam(name = "show", defaultValue = PARAM_SHOW_USERS_ACTIVE) String show,
                            Model model) {
        List<User> users;
        if (show.equals(PARAM_SHOW_USERS_ALL)) {
            users = userService.findAll();
        } else if (show.equals(PARAM_SHOW_USERS_DELETED)) {
            users = userService.findAll(Boolean.TRUE);
        } else {
            users = userService.findAll(Boolean.FALSE);
        }
        List<UserDto> usersDto = users.stream()
                        .map(mapper::userToDto)
                        .collect(toList());
        model.addAttribute("bankUsers", usersDto);

        return USER_NAMESPACE + USERS_PAGE;
    }

    @GetMapping("/{id}")
    public String showUser(@PathVariable("id") long id, Model model) {
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isEmpty()) {
            model.addAttribute("bankUser", new UserDto());
            log.error("!Added empty user to the model!");
        } else {
            model.addAttribute("bankUser", mapper.userToDto(userOpt.get()));
            log.info("Added user to model.");
        }

        return USER_NAMESPACE + USER_PAGE;
    }

    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isEmpty()) {
            model.addAttribute("bankUser", new UserDto());
            log.error("!Added empty user to the model!");
        } else {
            model.addAttribute("bankUser", mapper.userToDto(userOpt.get()));
            log.info("Added user to model.");
        }

        return REDIRECT + USERS_PAGE;
    }

    @GetMapping("/{id}/edit")
    public String showEditUser(@PathVariable("id") long id, Model model) {
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isEmpty()) {
            log.error("!Redirecting to \"/users\"!");
            return "redirect:/users";
        } else {
            model.addAttribute("bankUser", mapper.userToDto(userOpt.get()));
            log.info("Added user to model.");
        }

        return USER_NAMESPACE + EDIT_USER_PAGE;
    }

    @GetMapping("/add-default")
    public String addDefaultUser() {
        User user = defaultUserSupplier.get();
        userService.createUser(user);
        log.info("Redirecting to \"/users\"");

        return REDIRECT + USERS_PAGE;
    }

    @GetMapping("/add-random")
    public String addRandomUser() {
        User user = randomUserGenerator.get();
        userService.createUser(user);
        log.info("Redirecting to \"/users\"");

        return "redirect:/users";
    }

    @GetMapping("/add-user")
    public String showAddUser(Model model) {
        model.addAttribute("bankUser", new UserDto());
        log.info("Displaying \"add-user\" page.");

        return USER_NAMESPACE + ADD_USER_PAGE;
    }

    @PostMapping()
    public String addUser(@ModelAttribute("bankUser") UserDto bankUserDto) {
        userService.createUser(mapper.dtoToUser(bankUserDto));
        log.info("Redirecting to \"/users\"");

        return REDIRECT + USERS_PAGE;
    }

    @PutMapping("/{id}")
    public String editUser(@PathVariable("id") long id, @ModelAttribute("bankUser") UserDto bankUserDto) {
        userService.editUser(mapper.dtoToUser(bankUserDto), id);
        log.info("Redirecting to \"/users/" + id + "\"");

        return REDIRECT + USERS_PAGE + "/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        log.info("Redirecting to \"/users\"");

        return REDIRECT + USERS_PAGE;
    }

    @GetMapping("/add-default-account")
    public String addDefaultAccount() {
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
        log.info("Redirecting to \"/users\"");

        return REDIRECT + USERS_PAGE;
    }

    @GetMapping("/clear-all")
    public String clearAll() {
        accountRepo.deleteAll();
        log.info("Cleared all records from \"account\" table.");
        userService.findAll()
                .forEach(userService::deleteUser);
        log.info("Cleared all records from \"bank_user\" table.");
        log.info("Redirecting to \"/users\"");

        return REDIRECT + USERS_PAGE;
    }

}
