package com.epam.saturn.operator.service.account.transfers;

import com.epam.saturn.operator.dao.Account;
import com.epam.saturn.operator.service.account.TransactionResult;
import com.epam.saturn.operator.service.validation.Validator;

import java.math.BigDecimal;

import static com.epam.saturn.operator.service.validation.ValidationStrings.accountNumberRegexp;
import static com.epam.saturn.operator.service.validation.ValidationStrings.cardNumberRegexp;
import static com.epam.saturn.operator.service.validation.ValidationStrings.phoneNumberRegexp;

public enum TransferContextSingleton {

    TRANSFER_CONTEXT_SINGLETON;

    public TransactionResult transferByPhone(Account srcAccount, String phone, BigDecimal amount) {
        System.out.println("this must transfer money by phone");
        if (Validator.validate(phoneNumberRegexp, phone)){
            //TODO: implement
            return new TransactionResult();
        }
        throw new IllegalArgumentException("phone number didn't pass validation");
    }

    public TransactionResult transferByCard(Account srcAccount, String card, BigDecimal amount) {
        System.out.println("this must transfer money by card");
        if (Validator.validate(cardNumberRegexp, card)){
            //TODO: implement
            return new TransactionResult();
        }
        throw new IllegalArgumentException("card number didn't pass validation");
    }

    public TransactionResult transferByAccount(Account srcAccount, String account, BigDecimal amount) {
        System.out.println("this must transfer money by account number");
        if (Validator.validate(accountNumberRegexp, account)){
            //TODO: implement
            return new TransactionResult();
        }
        throw new IllegalArgumentException("account number didn't pass validation");
    }

}
