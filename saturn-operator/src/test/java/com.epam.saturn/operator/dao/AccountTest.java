package com.epam.saturn.operator.dao;

import com.epam.saturn.OperatorApp;
import com.epam.saturn.operator.controller.DefaultAccountSupplier;
import com.epam.saturn.operator.controller.DefaultUserSupplier;
import com.epam.saturn.operator.repository.AccountRepository;
import com.epam.saturn.operator.repository.CardRepository;
import com.epam.saturn.operator.repository.TransactionRepository;
import com.epam.saturn.operator.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OperatorApp.class)
public class AccountTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @BeforeEach
    public void init() {
        transactionRepository.deleteAll();
        cardRepository.deleteAll();
        userRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    public void createAccount_defaultAccountCreation_addedNewDefaultAccount() {
        User user = addDefaultUser();
        Account account = addDefaultAccount(user);
        List<Account> accountList = accountRepository.findAll();
        Assertions.assertTrue(accountList.size() > 0);
        Assertions.assertEquals(account.getId(), accountList.get(0).getId());
    }

    @Test
    public void deleteAllAccounts_notNullAccountsErasing_emptyTable() {
        User user = addDefaultUser();
        Account account = addDefaultAccount(user);
        accountRepository.deleteAll();
        boolean actual = accountRepository
                .findAll()
                .stream()
                .findAny()
                .isEmpty();
        Assertions.assertTrue(actual);
    }


    private User addDefaultUser() {
        User user = new DefaultUserSupplier().get();
        userRepository.save(user);
        return user;
    }

    private Account addDefaultAccount(User user){
        Account account = new DefaultAccountSupplier().get();
        account.setUser(user);
        accountRepository.save(account);
        return account;
    }

}