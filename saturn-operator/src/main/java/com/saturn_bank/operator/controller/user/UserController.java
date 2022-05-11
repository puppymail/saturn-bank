package com.saturn_bank.operator.controller.user;

import static com.saturn_bank.operator.controller.Uris.REDIRECT;
import static com.saturn_bank.operator.controller.Uris.SLASH;
import static com.saturn_bank.operator.controller.Uris.USERS_URI;
import static com.saturn_bank.operator.exception.ExceptionErrorMessages.NO_SUCH_ENTITY_EX_MSG;
import static java.util.stream.Collectors.toList;

import com.saturn_bank.operator.controller.RandomUserGenerator;
import com.saturn_bank.operator.dao.Account;
import com.saturn_bank.operator.dao.User;
import com.saturn_bank.operator.dto.UserDto;
import com.saturn_bank.operator.dto.mapper.UserMapper;
import com.saturn_bank.operator.exception.EntityAlreadyPresentException;
import com.saturn_bank.operator.exception.NoSuchEntityException;
import com.saturn_bank.operator.repository.AccountRepository;

import com.saturn_bank.operator.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

// TODO: refactor logging
@Slf4j
@Controller
@RequestMapping(USERS_URI)
public class UserController {

    private static final String DELETE_URI = "/delete";
    private static final String EDIT_URI = "/edit";
    private static final String ADD_URI = "/add-user";
    private static final String ADD_RANDOM_URI = "/add-random";
    private static final String ID_PATH_VAR_URI = "/{id}";
    private static final String ID_PATH_VAR = "id";

    private static final String USER_NAMESPACE = "user";
    private static final String USERS_PAGE = USER_NAMESPACE + "/users";
    private static final String USER_PAGE = USER_NAMESPACE + "/user";
    private static final String EDIT_USER_PAGE = USER_NAMESPACE + "/editUser";
    private static final String ADD_USER_PAGE = USER_NAMESPACE + "/addUser";

    private static final String USER_ATTR_NAME = "bankUser";
    private static final String USERS_ATTR_NAME = "bankUsers";
    private static final String PARAM_SHOW = "show";
    private static final String PARAM_SHOW_ALL = "all";
    private static final String PARAM_SHOW_DELETED = "deleted";
    private static final String PARAM_SHOW_ACTIVE = "active";

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
    public String showUsers(@RequestParam(name = PARAM_SHOW, defaultValue = PARAM_SHOW_ACTIVE) String show,
                            Model model) {
        List<User> users;
        if (show.equals(PARAM_SHOW_ACTIVE)) {
            users = userService.findAll(Boolean.FALSE);
        } else if (show.equals(PARAM_SHOW_DELETED)) {
            users = userService.findAll(Boolean.TRUE);
        } else {
            users = userService.findAll();
        }
        List<UserDto> usersDto = users.stream()
                        .map(mapper::userToDto)
                        .collect(toList());
        model.addAttribute(USERS_ATTR_NAME, usersDto);

        return USERS_PAGE;
    }

    @GetMapping(ID_PATH_VAR_URI)
    public String showUser(@PathVariable(ID_PATH_VAR) long id, Model model) throws NoSuchEntityException {
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isEmpty()) {
            throw new NoSuchEntityException(NO_SUCH_ENTITY_EX_MSG);
        }
        model.addAttribute(USER_ATTR_NAME, mapper.userToDto(userOpt.get()));

        return USER_PAGE;
    }

    // TODO: is this endpoint even used?
    @GetMapping(ID_PATH_VAR_URI + DELETE_URI)
    public String deleteUser(@PathVariable(ID_PATH_VAR) long id, Model model) {
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isEmpty()) {
            model.addAttribute(USER_ATTR_NAME, new UserDto());
        } else {
            model.addAttribute(USER_ATTR_NAME, mapper.userToDto(userOpt.get()));
        }

        return REDIRECT + USERS_URI;
    }

    @GetMapping(ID_PATH_VAR_URI + EDIT_URI)
    public String showEditUser(@PathVariable(ID_PATH_VAR) long id, Model model) throws NoSuchEntityException {
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isEmpty()) {
            throw new NoSuchEntityException(NO_SUCH_ENTITY_EX_MSG);
        }
        model.addAttribute(USER_ATTR_NAME, mapper.userToDto(userOpt.get()));

        return EDIT_USER_PAGE;
    }

    // TODO: remove
    @GetMapping("/add-default")
    public String addDefaultUser() throws EntityAlreadyPresentException {
        User user = defaultUserSupplier.get();
        try {
            userService.createUser(user);
        } catch (EntityAlreadyPresentException eape) {
            return REDIRECT + USERS_URI;
        }

        return REDIRECT + USERS_URI;
    }

    @GetMapping(ADD_RANDOM_URI)
    public String addRandomUser() {
        User user;
        do {
            user = randomUserGenerator.get();
            try {
                userService.createUser(user);
                break;
            } catch (EntityAlreadyPresentException e) {
                continue;
            }
        } while(true);

        return REDIRECT + USERS_URI;
    }

    @GetMapping(ADD_URI)
    public String showAddUser(Model model) {
        model.addAttribute(USER_ATTR_NAME, new UserDto());

        return ADD_USER_PAGE;
    }

    @PostMapping()
    public String addUser(@ModelAttribute(USER_ATTR_NAME) @Valid UserDto userDto, BindingResult br)
            throws EntityAlreadyPresentException {
        if (br.hasErrors()) {
            return ADD_USER_PAGE;
        }
        userService.createUser(mapper.dtoToUser(userDto));

        return REDIRECT + USERS_URI;
    }

    @PutMapping(ID_PATH_VAR_URI)
    public String editUser(@PathVariable(ID_PATH_VAR) long id,
                           @ModelAttribute(USER_ATTR_NAME) @Valid UserDto userDto,
                           BindingResult br) throws NoSuchEntityException {
        if (br.hasErrors()) {
            return EDIT_USER_PAGE;
        }
        userService.editUser(mapper.dtoToUser(userDto), id);

        return REDIRECT + USERS_URI + SLASH + id;
    }

    @DeleteMapping(ID_PATH_VAR_URI)
    public String deleteUser(@PathVariable(ID_PATH_VAR) long id) throws NoSuchEntityException {
        userService.deleteUser(id);

        return REDIRECT + USERS_URI;
    }

    // TODO: remove
    @GetMapping("/add-default-account")
    public String addDefaultAccount() {
        Account account = new Account();
        account.setUser(userService.findAll()
                .stream()
                .findAny()
                .orElseGet(() -> {
            User user = defaultUserSupplier.get();
            log.warn("Couldn't find existing user, inserting new default user in \"bank_user\" table!");
                    try {
                        userService.createUser(user);
                    } catch (EntityAlreadyPresentException e) {
                        throw new RuntimeException(e);
                    }
                    return user;
        }));

        accountRepo.save(account);
        log.info("Default account inserted in \"account\" table belonging to user id=" + account.getUser().getId());

        return REDIRECT + USERS_URI;
    }

    // TODO: remove
    @GetMapping("/clear-all")
    public String clearAll() {
        accountRepo.deleteAll();
        log.info("Cleared all records from \"account\" table.");
        userService.findAll()
                .forEach(user -> {
                    try {
                        userService.deleteUser(user);
                    } catch (NoSuchEntityException e) {
                        throw new RuntimeException(e);
                    }
                });
        log.info("Cleared all records from \"bank_user\" table.");

        return REDIRECT + USERS_URI;
    }

}
