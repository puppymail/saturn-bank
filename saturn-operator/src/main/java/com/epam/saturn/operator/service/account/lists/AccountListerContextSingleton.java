package com.epam.saturn.operator.service.account.lists;

import com.epam.saturn.operator.dao.Account;
import com.epam.saturn.operator.service.validation.Validator;

import java.util.ArrayList;
import java.util.List;

import static com.epam.saturn.operator.service.validation.ValidationStrings.cardNumberRegexp;
import static com.epam.saturn.operator.service.validation.ValidationStrings.phoneNumberRegexp;

public enum AccountListerContextSingleton {

    ACCOUNT_LISTER_CONTEXT_SINGLETON;

    public List<Account> showListByPhone(String phone) {
        System.out.println("this must print accounts by phone");
        if (Validator.validate(phoneNumberRegexp, phone)){
            //TODO: implement
            return new ArrayList<>();
        }
        throw new IllegalArgumentException("phone number didn't pass validation");
    }

    public List<Account> showListByCard(String card) {
        System.out.println("this must print accounts by card");
        if (Validator.validate(cardNumberRegexp, card)){
            //TODO: implement
            return new ArrayList<>();
        }
        throw new IllegalArgumentException("card number didn't pass validation");
    }
}
