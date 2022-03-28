package com.epam.saturn.operator.controller;

import com.epam.saturn.operator.dao.Account;
import com.epam.saturn.operator.dao.User;
import com.epam.saturn.operator.repository.AccountRepository;
import com.epam.saturn.operator.repository.CardRepository;
import com.epam.saturn.operator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.List;

@Controller
@RequestMapping("/accounts")
public class AccountViewTestController {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    @Autowired
    public AccountViewTestController(AccountRepository accountRepository, UserRepository userRepository, CardRepository cardRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
    }

    @GetMapping("/")
    public String testListView(Model model) {
        List<Account> accounts = accountRepository.findAll();
        model.addAttribute("accounts", accounts);
        return "accounts/list";
    }

    @GetMapping("/add")
    public String testAddAccount() {
        addNewAccount();
        return "redirect:/accounts/";
    }

    @GetMapping("/clear")
    public String testClearAccounts() {
        accountRepository.deleteAll();
        return "redirect:/accounts/";
    }

    private void addNewAccount() {
        Account account = new DefaultAccountSupplier().get();
        account.setUser(userRepository.findAll()
                .stream()
                .findAny()
                .orElseGet(() -> {
                    User user = new DefaultUserSupplier().get();
                    userRepository.save(user);
                    return user;
                }));
        accountRepository.save(account);
    }
}
