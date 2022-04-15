package com.epam.saturn.operator.dao;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Properties;

@Slf4j
public class CardFactory {

    private static final String DEFAULT_CARD_DATA_PROPERTIES = "defaultCardData.properties";

    private static final String DEFAULT_NUMBER;
    private static final String DEFAULT_PINCODE;
    private static final String DEFAULT_CVV2;
    private static final int DEFAULT_VALIDITY_PERIOD;

    static {
        Properties defaultCardData = new Properties();
        try (
                InputStream propsFileStream = CardFactory.class
                        .getClassLoader()
                        .getResourceAsStream(DEFAULT_CARD_DATA_PROPERTIES)
        ) {
            defaultCardData.load(propsFileStream);
        } catch (IOException e) {
            log.error("!Couldn't load default card data!");
            e.printStackTrace();
        }
        DEFAULT_NUMBER = defaultCardData.getProperty("number");
        DEFAULT_PINCODE = defaultCardData.getProperty("pincode");
        DEFAULT_CVV2 = defaultCardData.getProperty("cvv2");
        DEFAULT_VALIDITY_PERIOD = Integer.parseInt(defaultCardData.getProperty("validityPeriod"));
    }

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
