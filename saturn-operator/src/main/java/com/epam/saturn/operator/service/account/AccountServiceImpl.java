package com.epam.saturn.operator.service.account;

import com.epam.saturn.operator.dao.Account;
import com.epam.saturn.operator.dao.User;
import com.epam.saturn.operator.dao.AccountType;
import com.epam.saturn.operator.dao.AccountCoin;
import com.epam.saturn.operator.dao.Transaction;
import com.epam.saturn.operator.dto.AccountDto;
import com.epam.saturn.operator.repository.AccountRepository;
import com.epam.saturn.operator.repository.UserRepository;
import com.epam.saturn.operator.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository, UserService userService) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public Account openAccount(AccountDto accountDto) {
        final Long id = accountDto.getUserId();
        final User user = getUserById(id);
        return openAccount(user,
                accountDto.getIsDefault(),
                accountDto.getType(),
                accountDto.getCoin(),
                accountDto.getPercent());
    }

    private Account openAccount(User user, Boolean isDefault, AccountType type, AccountCoin coin, BigDecimal percent) {
        Account account = Account.builder()
                    .number(AccountNumberProvider.getDefaultAccountNumber())
                    .user(user)
                    .cards(new ArrayList<>())
                    .type(type)
                    .percent(percent)
                    .amount(new BigDecimal("0.0"))
                    .coin(coin)
                    .build();
        accountRepository.save(account);
        setAccountNumber(account, type, coin);
        setDefaultStatus(account, user, isDefault);
        return account;
    }

    @Override
    public void setAccountDefault(Long idAcc) {
        final Account account = getAccountById(idAcc);
        setDefaultStatus(account, account.getUser(), true);
    }

    @Override
    public void closeAccount(Account account) {
        checkAccountExistence(account);
        if (account.isDefault()) {
            User user = account.getUser();
            checkUserExistence(user);
            getActiveUserAccounts(user)
                    .stream()
                    .filter(accDif -> accDif.getId().longValue() != account.getId().longValue())
                    .findFirst()
                    .ifPresent(acc -> {
                        acc.setDefault(true);
                        accountRepository.save(acc);
                    });
        }
        accountRepository.delete(account);
    }

    @Override
    public TransactionResult depositMoney(Account account, BigDecimal amount) {
        //TODO: implement
        throw new UnsupportedOperationException();
    }

    @Override
    public TransactionResult withdrawMoney(Account account, BigDecimal amount) {
        //TODO: implement
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Transaction> getAccountTransactionHistory(Account account) {
        //TODO: implement
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Account> getAllUserAccounts(User user) {
        checkUserExistence(user);
        return accountRepository.findAllAccounts(user);
    }

    @Override
    public List<Account> getActiveUserAccounts(User user) {
        checkUserExistence(user);
        return accountRepository.findByUser(user);
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> getAccount(Long id) {
        return accountRepository.findById(id);
    }

    private void setDefaultStatus(Account account, User user, Boolean isDefault) {
        if (isDefault) {
            setAllAccountsNotDefault(account, user);
            account.setDefault(true);
        } else {
            if (noDefaultAccount(user)) {
                account.setDefault(true);
            }
        }
        accountRepository.save(account);
    }

    private boolean noDefaultAccount(User user) {
        return getActiveUserAccounts(user).stream().noneMatch(Account::isDefault);
    }

    private void setAllAccountsNotDefault(Account account, User user) {
        getActiveUserAccounts(user)
                .stream()
                .filter(Account::isDefault)
                .forEach(accDefault -> {
                    accDefault.setDefault(false);
                    accountRepository.save(accDefault);
                });
    }

    private void setAccountNumber(Account account, AccountType type, AccountCoin coin) {
        final String accountNumber = AccountNumberProvider.getAccountNumber(account.getId(), type, coin);
        account.setNumber(accountNumber);
        accountRepository.save(account);
    }

    private void checkUserExistence(User user) {
        if (!userRepository.exists(Example.of(user))) {
            throw new NoSuchElementException("User not found.");
        }
    }

    private void checkAccountExistence(Account account) {
        if (!accountRepository.exists(Example.of(account))) {
            throw new NoSuchElementException("Account not found.");
        }
    }

    private Account getAccountById(Long id) {
        Optional<Account> accountOptional = getAccount(id);
        if (accountOptional.isEmpty()) {
            throw new NoSuchElementException("Account not found.");
        }
        return accountOptional.get();
    }

    private User getUserById(Long id) {
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isEmpty()) {
            throw new NoSuchElementException("User not found.");
        }
        return userOptional.get();
    }
}
