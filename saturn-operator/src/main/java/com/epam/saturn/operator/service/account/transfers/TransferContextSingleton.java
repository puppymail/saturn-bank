package com.epam.saturn.operator.service.account.transfers;

import com.epam.saturn.operator.dao.Account;
import com.epam.saturn.operator.dao.TransactionType;
import com.epam.saturn.operator.dto.TransactionResult;
import com.epam.saturn.operator.repository.AccountRepository;
import com.epam.saturn.operator.repository.CardRepository;
import com.epam.saturn.operator.service.TransactionService;
import com.epam.saturn.operator.service.user.UserService;
import com.epam.saturn.operator.service.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.EnumSet;

import static com.epam.saturn.operator.service.validation.ValidationStrings.accountNumberRegexp;
import static com.epam.saturn.operator.service.validation.ValidationStrings.cardNumberRegexp;
import static com.epam.saturn.operator.service.validation.ValidationStrings.phoneNumberRegexp;

public enum TransferContextSingleton {

    TRANSFER_CONTEXT_SINGLETON;

    private TransactionService transactionService;
    private AccountRepository accountRepository;
    private UserService userService;
    private CardRepository cardRepository;

    @Component
    public static class TransferContextSingletonServiceInjector {

        @Autowired
        private TransactionService transactionService;
        @Autowired
        private AccountRepository accountRepository;
        @Autowired
        private UserService userService;
        @Autowired
        private CardRepository cardRepository;

        @PostConstruct
        public void postConstruct() {
            for (TransferContextSingleton tcs : EnumSet.allOf(TransferContextSingleton.class)) {
                tcs.setTransactionService(transactionService);
                tcs.setAccountRepository(accountRepository);
                tcs.setUserService(userService);
                tcs.setCardRepository(cardRepository);
            }
        }
    }

    public TransactionResult transferByPhone(Account srcAccount, String phone, BigDecimal amount, String purpose, TransactionType type) {
        if (Validator.validate(phoneNumberRegexp, phone)) {

            String dstNumber = userService.findByPhoneNumber(phone)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No user with this phone number at DB"))
                    .getAccountList()
                    .stream()
                    .filter(Account::isDefault)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("User with this phone doesn't have default account"))
                    .getNumber();
            return transactionService.transfer(srcAccount.getNumber(), dstNumber, amount, purpose, type);
        }
        throw new IllegalArgumentException("phone number didn't pass validation");
    }

    public TransactionResult transferByCard(Account srcAccount, String card, BigDecimal amount, String purpose, TransactionType type) {
        if (Validator.validate(cardNumberRegexp, card)) {
            String dstNumber = cardRepository.findByNumber(card)
                    .orElseThrow(() -> new IllegalArgumentException("No such card number at DB"))
                    .getAccount()
                    .getNumber();
            return transactionService.transfer(srcAccount.getNumber(), dstNumber, amount, purpose, type);
        }
        throw new IllegalArgumentException("card number didn't pass validation");
    }

    public TransactionResult transferByAccount(Account srcAccount, String account, BigDecimal amount, String purpose, TransactionType type) {
        if (Validator.validate(accountNumberRegexp, account)) {
            String dstNumber = accountRepository.findByNumber(account)
                    .orElseThrow(() -> new IllegalArgumentException("No such destination account"))
                    .getNumber();
            return transactionService.transfer(srcAccount.getNumber(), dstNumber, amount, purpose, type);
        }
        throw new IllegalArgumentException("account number didn't pass validation");
    }

    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setCardRepository(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }
}
