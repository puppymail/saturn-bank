package com.epam.saturn.operator.service;

import com.epam.saturn.operator.dao.Account;
import com.epam.saturn.operator.dao.Card;
import com.epam.saturn.operator.dao.User;

public interface CardService {

    Card issueCard(Account account, User user);

    void changePinCodeByOperator(String cardNumber);

    void changePinCodeByClient(String cardNumber, String oldPinCode, String newPinCode);

    void closeCard(Card card);

    void closeCard(Long id);

}
