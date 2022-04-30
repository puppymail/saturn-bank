package com.saturn_bank.operator.service.account;

import com.saturn_bank.operator.dao.Account;
import com.saturn_bank.operator.dao.Transaction;
import com.saturn_bank.operator.dao.TransactionType;
import com.saturn_bank.operator.dao.AccountCoin;
import com.saturn_bank.operator.dao.AccountType;
import com.saturn_bank.operator.dao.User;
import com.saturn_bank.operator.dto.AccountDto;
import com.saturn_bank.operator.dto.TimeRange;
import com.saturn_bank.operator.dto.TransactionHistoryDto;
import com.saturn_bank.operator.repository.AccountRepository;
import com.saturn_bank.operator.repository.TransactionRepository;
import com.saturn_bank.operator.repository.UserRepository;
import com.saturn_bank.operator.service.TransactionService;
import com.saturn_bank.operator.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Comparator;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;
    private final Session session;
    private final BigDecimal INITIAL_ACCOUNT_AMOUNT = new BigDecimal("0.00");
    private final String FILTER_ONLY_ACTIVE_ACCOUNTS = "activeAccountsOnlyFilter";
    private final String TRANSACTION_PURPOSE_CLOSING_ACCOUNT = "From closed account";
    private final String WITHDRAW_TO_CASH_NUMBER = "99999999999999999999";
    private final String MONEY_TRANSFER_BY_ACCOUNT = "BY_ACCOUNT";

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository,
                              UserRepository userRepository,
                              UserService userService,
                              TransactionService transactionService,
                              TransactionRepository transactionRepository,
                              EntityManager entityManager) {

        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.transactionService=transactionService;
        this.transactionRepository = transactionRepository;
        session = entityManager.unwrap(Session.class);
    }

    @Transactional
    @Override
    public Account openAccount(AccountDto accountDto) {
        final Long id = accountDto.getUserId();
        final User user = getUserById(id);
        Account account = Account.builder()
                .number(AccountNumberProvider.getDefaultAccountNumber())
                .user(user)
                .cards(new ArrayList<>())
                .type(accountDto.getType())
                .percent(accountDto.getPercent())
                .amount(INITIAL_ACCOUNT_AMOUNT)
                .coin(accountDto.getCoin())
                .build();
        accountRepository.save(account);
        setAccountNumber(account, accountDto.getType(), accountDto.getCoin());
        setDefaultStatus(account, user, accountDto.getIsDefault());
        return account;
    }

    @Override
    public void setAccountDefault(Long idAcc) {
        final Account account = getAccountById(idAcc);
        setDefaultStatus(account, account.getUser(), true);
    }

    @Transactional
    @Override
    public void closeAccount(Account account) {
        session.enableFilter(FILTER_ONLY_ACTIVE_ACCOUNTS);
        checkAccountExistence(account);
        User user = account.getUser();
        checkUserExistence(user);
        if (account.isDefault()) {
            allAccountsExceptForCurrent(user, account)
                    .stream()
                    .findFirst()
                    .ifPresent(acc -> {
                        acc.setDefault(true);
                        accountRepository.save(acc);
                    });
        }
        if (!account.getAmount().equals(INITIAL_ACCOUNT_AMOUNT)) {
            Optional<Account> dstClosingTransferOptional = allAccountsExceptForCurrent(user, account)
                    .stream()
                    .filter(Account::isDefault)
                    .findFirst();
            if(dstClosingTransferOptional.isEmpty()) {
                transfer(account,
                        WITHDRAW_TO_CASH_NUMBER,
                        account.getAmount(),
                        TRANSACTION_PURPOSE_CLOSING_ACCOUNT,
                        TransactionType.WITHDRAW,
                        MONEY_TRANSFER_BY_ACCOUNT);
            } else {
                Account dstClosingTransfer = dstClosingTransferOptional.get();
                transfer(account,
                        dstClosingTransfer.getNumber(),
                        account.getAmount(),
                        TRANSACTION_PURPOSE_CLOSING_ACCOUNT,
                        TransactionType.TRANSFER,
                        MONEY_TRANSFER_BY_ACCOUNT);
            }
        }
        //TODO: resolve @transactional problem better
        Account accountToDelete = getAccountById(account.getId());
        accountRepository.delete(accountToDelete);
    }

    private List<Account> allAccountsExceptForCurrent(User user, Account account) {
        return getActiveUserAccounts(user)
                .stream()
                .filter(accDif -> accDif.getId().longValue() != account.getId().longValue())
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionHistoryDto> getAccountTransactionsHistory(Account account, Boolean full) {
        session.disableFilter(FILTER_ONLY_ACTIVE_ACCOUNTS);
        checkAccountExistence(account);
        List<Transaction> transactions = getFullTransactionListForAccount(account);
        List<TransactionHistoryDto> transactionDtos = getTransactionDtoList(transactions);
        if (!transactionDtos.isEmpty() && !full) {
            TimeRange oneMonthRange = new TimeRange(LocalDate.now().minusDays(30), LocalDate.now());
            System.out.println(oneMonthRange);
            return limitTransactionsHistoryByTimeRange(transactionDtos, oneMonthRange);
        }
        return transactionDtos;
    }

    @Override
    public List<TransactionHistoryDto> getAccountTransactionsHistoryForTimeRange(Account account, TimeRange range) {
        session.disableFilter(FILTER_ONLY_ACTIVE_ACCOUNTS);
        checkAccountExistence(account);
        List<Transaction> transactions = getFullTransactionListForAccount(account);
        List<TransactionHistoryDto> transactionDtos = getTransactionDtoList(transactions);
        return limitTransactionsHistoryByTimeRange(transactionDtos, range);
    }

    private List<Transaction> getFullTransactionListForAccount(Account account) {
        session.disableFilter(FILTER_ONLY_ACTIVE_ACCOUNTS);
        List<Transaction> transactions = transactionRepository.findBySrc(account.getNumber());
        transactions.addAll(transactionRepository.findByDst(account.getNumber()));
        return transactions.stream()
                .sorted(Comparator.comparing(Transaction::getDateTime))
                .collect(Collectors.toList());
    }

    @Override
    public List<Account> getAllUserAccounts(User user) {
        session.disableFilter(FILTER_ONLY_ACTIVE_ACCOUNTS);
        checkUserExistence(user);
        return accountRepository.findAccountsByUser(user);
    }

    @Override
    public List<Account> getActiveUserAccounts(User user) {
        session.enableFilter(FILTER_ONLY_ACTIVE_ACCOUNTS);
        checkUserExistence(user);
        return accountRepository.findAccountsByUser(user);
    }

    @Override
    public List<Account> findAll() {
        session.enableFilter(FILTER_ONLY_ACTIVE_ACCOUNTS);
        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> findAccountByAccountId(Long id) {
        session.disableFilter(FILTER_ONLY_ACTIVE_ACCOUNTS);
        return accountRepository.findAccountById(id);
    }

    private List<TransactionHistoryDto> getTransactionDtoList(List<Transaction> transactions) {
        List<TransactionHistoryDto> transactionDtoList = new ArrayList<>();
        for (Transaction transaction : transactions) {
            transactionDtoList.add(TransactionHistoryDto.builder()
                    .id(transaction.getId())
                    .srcNumber(transaction.getSrc())
                    .dstNumber(transaction.getDst())
                    .amount(transaction.getAmount())
                    .purpose(transaction.getPurpose())
                    .dateTime(transaction.getDateTime())
                    .state(transaction.getState().name())
                    .build());
        }
        return transactionDtoList;
    }

    private List<TransactionHistoryDto> limitTransactionsHistoryByTimeRange(List<TransactionHistoryDto> transactionDtos, TimeRange range) {
        ChronoLocalDateTime<LocalDate> start = range.getStart().atStartOfDay();
        ChronoLocalDateTime<LocalDate> end = range.getEnd().plusDays(1).atStartOfDay();
        return transactionDtos.stream()
                .sorted(Comparator.comparing(TransactionHistoryDto::getDateTime))
                .filter(transactionDto -> transactionDto.getDateTime().isAfter(start))
                .filter(transactionDto -> transactionDto.getDateTime().isBefore(end))
                .collect(Collectors.toList());
    }

    private void setDefaultStatus(Account account, User user, Boolean isDefault) {
        if (isDefault) {
            setAllAccountsNotDefault(account, user);
            account.setDefault(true);
        } else {
            if (getDefaultAccount(user).isEmpty()) {
                account.setDefault(true);
            }
        }
        accountRepository.save(account);
    }

    private Optional<Account> getDefaultAccount(User user) {
        return getActiveUserAccounts(user).stream().filter(Account::isDefault).findFirst();
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
        Optional<Account> accountOptional = findAccountByAccountId(id);
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
