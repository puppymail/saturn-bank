package com.epam.saturn.operator.dao;

import java.time.LocalDate;

public class CardFactory {

    private static final String DEFAULT_NUMBER = "0000000000000000";
    private static final String DEFAULT_PINCODE = "0001";
    private static final String DEFAULT_CVV2 = "000";
    private static final int DEFAULT_VALIDITY_PERIOD = 4;

    public static Card createCard(Account account, User user) {
        return createCard(account, user, DEFAULT_VALIDITY_PERIOD);
    }

    public static Card createCard(Account account, User user, int cardValidityPeriod) {
        Card card = new Card();

        card.setAccount(account);
        card.setUser(user);
        card.setNumber(generateCardNumber());
        card.setCvv2(generateCVV2());
        card.setPinCode(DEFAULT_PINCODE);
        card.setIssueDate(LocalDate.now());
        card.setValidTill(card.getIssueDate().plusYears(cardValidityPeriod));

        return card;
    }

    // TODO implement card number generation
    protected static String generateCardNumber() {
        return DEFAULT_NUMBER;
    }

    // TODO implement CVV generation
    protected static String generateCVV2() {
        return DEFAULT_CVV2;
    }

}
