package com.epam.saturn.operator.service;

import com.epam.saturn.operator.dao.Account;
import com.epam.saturn.operator.dao.Transaction;
import com.epam.saturn.operator.dao.TransactionState;
import com.epam.saturn.operator.dto.TransactionResult;
import com.epam.saturn.operator.repository.AccountRepository;
import com.epam.saturn.operator.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepo;
    private final TransactionRepository transactionRepo;

    @Autowired
    public TransactionServiceImpl(AccountRepository accountRepo, TransactionRepository transactionRepo) {
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
    }

    @Override
    public TransactionResult transfer(long accountSrcId, long accountDstId, BigDecimal amount, String purpose) {
        Transaction transaction = new Transaction();
        transaction.setState(TransactionState.NEW);
        Account accountSrc = null;
        Account accountDst = null;
        try {
            accountSrc = accountRepo.findById(accountSrcId).orElseThrow();
        } catch (NoSuchElementException e) {
            transaction.setState(TransactionState.ERROR);
            System.err.println("No such source account at DB"); //TODO
        }
        try {
            accountDst = accountRepo.findById(accountDstId).orElseThrow();
        } catch (NoSuchElementException e) {
            transaction.setState(TransactionState.ERROR);
            System.err.println("No such destination account at DB"); //TODO
        }
        if (accountSrc != null && accountDst != null) {
            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                transaction.setState(TransactionState.ERROR);
            } else if (accountSrc.getAmount().compareTo(amount) < 0) {
                transaction.setState(TransactionState.CANCELLED);
            } else {
                accountSrc.setAmount(accountSrc.getAmount().subtract(amount));
                accountDst.setAmount(accountDst.getAmount().add(amount));
                accountRepo.save(accountSrc);
                accountRepo.save(accountDst);
                transaction.setState(TransactionState.DONE);
            }
        }
        transaction.setSrc(accountSrc);
        transaction.setDst(accountDst);
        transaction.setAmount(amount);
        transaction.setPurpose(purpose);
        transaction.setDateTime(LocalDateTime.now());
        transactionRepo.save(transaction);
        return new TransactionResult(transaction.getId(), transaction.getState());
    }
}
