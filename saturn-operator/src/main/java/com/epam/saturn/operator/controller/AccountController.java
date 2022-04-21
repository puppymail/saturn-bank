package com.epam.saturn.operator.controller;

import com.epam.saturn.operator.dao.Account;
import com.epam.saturn.operator.dao.User;
import com.epam.saturn.operator.dto.AccountDto;
import com.epam.saturn.operator.service.UserService;
import com.epam.saturn.operator.service.account.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final UserService userService;

    private final String ATTRIBUTE_BANK_USER= "bankUser"; 
    private final String ATTRIBUTE_ACCOUNTS = "accounts";
    private final String ATTRIBUTE_ACCOUNT = "account";
    private final String ATTRIBUTE_NO_ACCOUNT = "noAccount";

    private final String REDIRECT_USERS = "redirect:/users/";
    private final String REDIRECT_ACTIVE_USER_ACCOUNTS = "redirect:/accounts/active-accounts-for";
    private final String REDIRECT_ADD_ACCOUNT = "redirect:/add-account-for";

    private final String TEMPLATE_LIST_OF_ACCOUNTS_ALL_USERS = "accounts/listOfAccounts";
    private final String TEMPLATE_USER_ACCOUNTS = "accounts/userAccounts";
    private final String TEMPLATE_USER_ACCOUNT = "accounts/userAccount";
    private final String TEMPLATE_LIST_OF_USER_ACCOUNTS = "accounts/userAccountsAll";
    private final String TEMPLATE_OPEN_NEW_ACCOUNT = "accounts/openNewAccount";


    @Autowired
    public AccountController(AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String listOfAccounts(Model model) {
        List<Account> accounts = accountService.findAll();
        model.addAttribute(ATTRIBUTE_ACCOUNTS, accounts);
        List<User> users = userService.findAll();
        model.addAttribute(ATTRIBUTE_BANK_USER, users);
        return TEMPLATE_LIST_OF_ACCOUNTS_ALL_USERS;
    }

    @GetMapping("/accounts-for{id}")
    public String showUserAccountsForm(@PathVariable("id") Long id, Model model) {
        return tryAddUserToModel(id, model).orElse(TEMPLATE_USER_ACCOUNTS);
    }

    @GetMapping("/all-accounts-for{id}")
    public String showUserAccountsAll(@PathVariable("id") Long id, Model model) {
        final Optional<String> errResponse = tryAddUserToModel(id, model);
        if (errResponse.isPresent()) {
            return errResponse.get();
        }

        User user = (User) model.getAttribute(ATTRIBUTE_BANK_USER);
        List<Account> accounts = accountService.getAllUserAccounts(user);
        model.addAttribute(ATTRIBUTE_ACCOUNTS, accounts);
        return TEMPLATE_LIST_OF_USER_ACCOUNTS;
    }

    @GetMapping("/all-accounts-for{id}/{idAcc}")
    public String showUserAccount(@PathVariable("id") Long id, @PathVariable("idAcc") Long idAcc, Model model) {
        final Optional<String> errResponse = tryAddUserToModel(id, model);
        if (errResponse.isPresent()) {
            return errResponse.get();
        }

        addAccountToModel(idAcc, model);
        return TEMPLATE_USER_ACCOUNT;
    }

    @PutMapping("/all-accounts-for{id}/{idAcc}")
    public String setAccountDefault(@PathVariable("id") Long id, @PathVariable("idAcc") Long idAcc, Model model) {
        final Optional<String> errResponse = tryAddUserToModel(id, model);
        if (errResponse.isPresent()) {
            return errResponse.get();
        }

        addAccountToModel(idAcc, model);
        if (model.containsAttribute(ATTRIBUTE_ACCOUNT)) {
            accountService.setAccountDefault(idAcc);
        }
        return TEMPLATE_USER_ACCOUNT;
    }

    @DeleteMapping("/all-accounts-for{id}/{idAcc}")
    public String deleteUserAccount(@PathVariable("id") Long id, @PathVariable("idAcc") Long idAcc, Model model) {
        Optional<Account> accountOptional = accountService.getAccount(idAcc);
        if (accountOptional.isEmpty()) {
            model.addAttribute(ATTRIBUTE_NO_ACCOUNT, Boolean.TRUE);
            return TEMPLATE_USER_ACCOUNT;
        }
        accountService.closeAccount(accountOptional.get());
        return REDIRECT_ACTIVE_USER_ACCOUNTS + id;
    }

    @GetMapping("/active-accounts-for{id}")
    public String showUserAccountsActive(@PathVariable("id") Long id, Model model) {
        final Optional<String> errResponse = tryAddUserToModel(id, model);
        if (errResponse.isPresent()) {
            return errResponse.get();
        }

        User user = (User) model.getAttribute(ATTRIBUTE_BANK_USER);
        List<Account> accounts = accountService.getActiveUserAccounts(user);
        model.addAttribute(ATTRIBUTE_ACCOUNTS, accounts);
        return TEMPLATE_LIST_OF_USER_ACCOUNTS;
    }

    @GetMapping("/add-account-for{id}")
    public String showOpenAccountForm(@PathVariable("id") Long id, Model model) {
        return tryAddUserToModel(id, model).orElse(TEMPLATE_OPEN_NEW_ACCOUNT);
    }

    @PostMapping("/add-account-for{id}")
    public String openAccount(@PathVariable("id") Long id,
                              @ModelAttribute("accountDto") AccountDto accountDto,
                              BindingResult bindingResult,
                              Model model) {
        final Optional<String> errResponse = tryAddUserToModel(id, model);
        if (errResponse.isPresent()) {
            return errResponse.get();
        }

        if (bindingResult.hasErrors()) {
            return REDIRECT_ADD_ACCOUNT + id;
        }
        accountService.openAccount(accountDto);
        return REDIRECT_ACTIVE_USER_ACCOUNTS + id;
    }

    @ModelAttribute("accountDto")
    public AccountDto getDefaultAccountDto() {
        return new AccountDto();
    }

    private void addAccountToModel(Long idAcc, Model model) {
        Optional<Account> accountOptional = accountService.getAccount(idAcc);
        if (accountOptional.isPresent()) {
            model.addAttribute(ATTRIBUTE_ACCOUNT, accountOptional.get());
        } else {
            model.addAttribute(ATTRIBUTE_NO_ACCOUNT, Boolean.TRUE);
        }
    }

    /**
     * @return empty response if user found, and redirect response if not found
     */
    private Optional<String> tryAddUserToModel(Long id, Model model) {
        final Optional<User> userOptional = userService.findById(id);
        if (userOptional.isEmpty()) {
            return Optional.of(REDIRECT_USERS);
        }

        User user = userOptional.get();
        model.addAttribute(ATTRIBUTE_BANK_USER, user);

        return Optional.empty();
    }
}
