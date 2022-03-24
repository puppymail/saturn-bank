package com.epam.saturn.operator.controller;

import com.epam.saturn.operator.dao.Account;
import com.epam.saturn.operator.dao.Transaction;
import com.epam.saturn.operator.dao.TransactionState;
import com.epam.saturn.operator.dao.User;
import com.epam.saturn.operator.repository.AccountRepository;
import com.epam.saturn.operator.repository.TransactionRepository;
import com.epam.saturn.operator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private static final BigDecimal ACCOUNT_SRC_AMOUNT = new BigDecimal(500);
    private static final BigDecimal ACCOUNT_DST_AMOUNT = new BigDecimal(300);
    private static final BigDecimal TRANSACTION_AMOUNT = new BigDecimal(200);

    private final UserRepository userRepo;
    private final AccountRepository accountRepo;
    private final TransactionRepository transactionRepo;
    private final Supplier<User> userSupplier;

    @Autowired
    public TransactionController(AccountRepository accountRepo, TransactionRepository transactionRepo,
                                 UserRepository userRepo, Supplier<User> userSupplier) {
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
        this.userRepo = userRepo;
        this.userSupplier = userSupplier;
    }

    @GetMapping("/add")
    public String addTransaction() {
        Account accountSrc = new Account();
        accountSrc.setAmount(ACCOUNT_SRC_AMOUNT);
        accountSrc.setUser(userRepo.findAll()
                .stream()
                .findAny()
                .orElseGet(() -> {
                    User user = userSupplier.get();
                    userRepo.save(user);
                    return user;
                }));
        accountRepo.save(accountSrc);
        Account accountDst = new Account();
        accountDst.setAmount(ACCOUNT_DST_AMOUNT);
        accountDst.setUser(userRepo.findAll()
                .stream()
                .findAny()
                .orElseGet(() -> {
                    User user = userSupplier.get();
                    userRepo.save(user);
                    return user;
                }));
        accountRepo.save(accountDst);
        Transaction transaction = new Transaction();
        transaction.setSrc(accountSrc);
        transaction.setDst(accountDst);
        transaction.setAmount(TRANSACTION_AMOUNT);
        transaction.setDateTime(LocalDateTime.now());
        transaction.setState(TransactionState.DONE);
        transactionRepo.save(transaction);
        return "redirect:/transactions/";
    }

    @GetMapping("/print-all")
    public String transactionGetAll() {
        List<Account> accounts = accountRepo.findAll();
        System.out.println("Accounts:");
        accounts.forEach(System.out::println);
        List<Transaction> transactions = transactionRepo.findAll();
        System.out.println("Transactions:");
        transactions.forEach(System.out::println);
        return "redirect:/transactions/";
    }

    @GetMapping("/clear-all")
    public String clearAll() {
        transactionRepo.deleteAll();
        accountRepo.deleteAll();
        return "redirect:/transactions/";
    }

    @GetMapping("/clear-all-transactions")
    public String clearAllTransactions() {
        transactionRepo.deleteAll();
        return "redirect:/transactions/";
    }

}
