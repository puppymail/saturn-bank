package com.epam.saturn.operator.controller;

import com.epam.saturn.operator.dao.Account;
import com.epam.saturn.operator.dao.TransactionType;
import com.epam.saturn.operator.dao.User;
import com.epam.saturn.operator.dto.TransactionDto;
import com.epam.saturn.operator.repository.AccountRepository;
import com.epam.saturn.operator.repository.CardRepository;
import com.epam.saturn.operator.repository.UserRepository;
import com.epam.saturn.operator.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/accounts")
public class AccountViewTestController {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final TransactionService transactionService;

    @Autowired
    public AccountViewTestController(AccountRepository accountRepository, UserRepository userRepository, CardRepository cardRepository, TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        this.transactionService = transactionService;
    }

    @GetMapping("/add-transaction-by-service&src-id={accountSrcId}&dst-id={accountDstId}&amount={amount}&purpose={purpose}&type={type}")
    public String addTransactionByService(@PathVariable long accountSrcId, @PathVariable long accountDstId, @PathVariable BigDecimal amount, @PathVariable String purpose, @PathVariable int type) {
        transactionService.transfer(accountSrcId, accountDstId, amount, purpose, TransactionType.values()[type]);
        return "accounts/list";
    }

    @PostMapping("/add")
    public String addPostTransactionByService(@RequestBody TransactionDto transaction) {
        transactionService.transfer(transaction.getAccountSrc(), transaction.getAccountDst(), transaction.getAmount(), transaction.getPurpose(), transaction.getType());
        return "accounts/list";
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
