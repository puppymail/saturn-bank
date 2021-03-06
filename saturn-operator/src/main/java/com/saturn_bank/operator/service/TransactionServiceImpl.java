package com.saturn_bank.operator.service;

import com.saturn_bank.operator.dao.Account;
import com.saturn_bank.operator.dao.Transaction;
import com.saturn_bank.operator.dao.TransactionState;
import com.saturn_bank.operator.dao.TransactionType;
import com.saturn_bank.operator.dto.TransactionResult;
import com.saturn_bank.operator.repository.AccountRepository;
import com.saturn_bank.operator.repository.TransactionRepository;
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
    public TransactionResult transfer(String accountSrcNumber, String accountDstNumber, BigDecimal amount, String purpose, TransactionType type) {
        Transaction transaction = new Transaction();
        StringBuilder result = new StringBuilder();
        transaction.setState(TransactionState.NEW);
        Account accountSrc = null;
        Account accountDst = null;
        try {
            accountSrc = accountRepo.findByNumber(accountSrcNumber).orElseThrow();
        } catch (NoSuchElementException e) {
            transaction.setState(TransactionState.ERROR);
            result.append("No such src account in DB, ");
        }
        try {
            accountDst = accountRepo.findByNumber(accountDstNumber).orElseThrow();
        } catch (NoSuchElementException e) {
            transaction.setState(TransactionState.ERROR);
            result.append("No such dst account in DB, ");
        }
        if (accountSrc != null && accountDst != null) {
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                transaction.setState(TransactionState.ERROR);
                result.append("Amount can't be negative or zero, ");
            } else if (accountSrc.getCoin() != accountDst.getCoin()) {
                transaction.setState(TransactionState.CANCELLED);
                result.append("Src account and dst account have incompatible currency types, ");
            } else if (type != TransactionType.DEPOSIT && accountSrcNumber.equals(accountDstNumber)) {
                transaction.setState(TransactionState.CANCELLED);
                result.append("Src account and dst accounts are same");
            } else if (type != TransactionType.DEPOSIT && accountSrc.getAmount().compareTo(amount) < 0) {
                transaction.setState(TransactionState.CANCELLED);
                result.append("Src account doesn't have enough money for transfer, ");
            } else {
                if (type != TransactionType.DEPOSIT) {
                    accountSrc.setAmount(accountSrc.getAmount().subtract(amount));
                    accountRepo.save(accountSrc);
                }
                if (type != TransactionType.WITHDRAW) {
                    accountDst.setAmount(accountDst.getAmount().add(amount));
                    accountRepo.save(accountDst);
                }
                transaction.setState(TransactionState.DONE);
                result.append("Transaction completed successfully, ");
            }
        }
        transaction.setSrc(accountSrcNumber);
        transaction.setDst(accountDstNumber);
        transaction.setAmount(amount);
        transaction.setPurpose(purpose);
        transaction.setDateTime(LocalDateTime.now());
        transaction.setType(type);
        transactionRepo.save(transaction);
        return new TransactionResult(transaction.getId(), transaction.getState(), transaction.getType(), result.substring(0, result.length() - 2));
    }
}
