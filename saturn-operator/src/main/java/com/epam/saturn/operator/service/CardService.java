package com.epam.saturn.operator.service;

import com.epam.saturn.operator.dao.Account;
import com.epam.saturn.operator.dao.Card;
import com.epam.saturn.operator.dao.User;
import org.springframework.stereotype.Service;

public interface CardService {

    Card issueCard(Account account, User user);

    void changePinCodeByOperator(String cardNumber);

    void changePinCodeByClient(String cardNumber, String oldPinCode, String newPinCode);

}
