package com.epam.saturn.operator.service;

import com.epam.saturn.operator.dao.Account;
import com.epam.saturn.operator.dao.Card;
import com.epam.saturn.operator.dao.CardFactory;
import com.epam.saturn.operator.dao.User;
import com.epam.saturn.operator.repository.AccountRepository;
import com.epam.saturn.operator.repository.CardRepository;
import com.epam.saturn.operator.repository.UserRepository;
import com.epam.saturn.operator.service.validation.PinCodeValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository,
                           AccountRepository accountRepository,
                           UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Card issueCard(Account account, User user) {
        Account existingAccount = accountRepository.findOne(Example.of(account))
                .orElseThrow();
        User cardUser = userRepository.findOne(Example.of(user))
                .orElseThrow();
        Card card = CardFactory.createCard(existingAccount, cardUser);
        cardRepository.save(card);

        return card;
    }

    public Card issueCard(Account account) {
        return this.issueCard(account, account.getUser());
    }

    @Override
    public void changePinCodeByOperator(String cardNumber) {
        Card existingCard = cardRepository.findByNumber(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("No such card number at DB"));
        existingCard.setPinCode(generatePinCode());
        cardRepository.save(existingCard);
        log.info("Card pincode was changed at card with number: " + existingCard.getNumber());

    }

    @Override
    public void changePinCodeByClient(String cardNumber, String oldPinCode, String newPinCode) {
        Card existingCard = cardRepository.findByNumber(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("No such card number at DB"));
        PinCodeValidator.validatePinCode(existingCard.getPinCode(), oldPinCode, newPinCode);
        existingCard.setPinCode(newPinCode);
        cardRepository.save(existingCard);
        log.info("Card pincode was changed at card with number: " + existingCard.getNumber());

    }

    private String generatePinCode() {
        int range = 9999;
        return String.format("%04d", new Random().nextInt(range));
    }

}
