package com.saturn_bank.operator.service.account.lists;

import com.saturn_bank.operator.dao.Account;
import com.saturn_bank.operator.service.validation.Validator;
import com.saturn_bank.operator.service.validation.ValidationStrings;

import java.util.ArrayList;
import java.util.List;

public enum AccountListerContextSingleton {

    ACCOUNT_LISTER_CONTEXT_SINGLETON;

    public List<Account> showListByPhone(String phone) {
        System.out.println("this must print accounts by phone");
        if (Validator.validate(ValidationStrings.phoneNumberRegexp, phone)){
            //TODO: implement
            return new ArrayList<>();
        }
        throw new IllegalArgumentException("phone number didn't pass validation");
    }

    public List<Account> showListByCard(String card) {
        System.out.println("this must print accounts by card");
        if (Validator.validate(ValidationStrings.cardNumberRegexp, card)){
            //TODO: implement
            return new ArrayList<>();
        }
        throw new IllegalArgumentException("card number didn't pass validation");
    }
}
