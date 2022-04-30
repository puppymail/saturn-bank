package com.saturn_bank.operator.controller;

import com.saturn_bank.operator.dao.Account;
import com.saturn_bank.operator.dao.Transaction;
import com.saturn_bank.operator.repository.AccountRepository;
import com.saturn_bank.operator.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private final AccountRepository accountRepo;
    private final TransactionRepository transactionRepo;

    @Autowired
    public TransactionController(AccountRepository accountRepo, TransactionRepository transactionRepo) {
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
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
