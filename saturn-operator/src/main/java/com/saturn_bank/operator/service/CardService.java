package com.saturn_bank.operator.service;

import com.saturn_bank.operator.dao.Account;
import com.saturn_bank.operator.dao.Card;
import com.saturn_bank.operator.dao.User;

public interface CardService {

    Card issueCard(Account account, User user);

    void changePinCodeByOperator(String cardNumber);

    void changePinCodeByClient(String cardNumber, String oldPinCode, String newPinCode);

    void closeCard(Card card);

    void closeCard(Long id);

}
