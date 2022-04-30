package com.saturn_bank.operator.dao;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Properties;
import java.util.Random;

@Slf4j
public class CardFactory {

    private static final String DEFAULT_CARD_DATA_PROPERTIES = "defaultCardData.properties";

    private static final int PIN_CODE_RANGE = 9999;
    private static final int CVV2_RANGE = 999;

    private static final String BIN_NUMBER;
    private static final String IDENTITY_DEFAULT_NUMBER;
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
        BIN_NUMBER = defaultCardData.getProperty("bin_number");
        IDENTITY_DEFAULT_NUMBER = defaultCardData.getProperty("identity_default_number");
        DEFAULT_VALIDITY_PERIOD = Integer.parseInt(defaultCardData.getProperty("validityPeriod"));
    }

    public static Card createCard(Account account, User user) {
        return createCard(account, user, DEFAULT_VALIDITY_PERIOD);
    }

    public static Card createCard(Account account, User user, int cardValidityPeriod) {
        Card card = new Card();

        card.setAccount(account);
        card.setUser(user);
        card.setNumber(generateCardNumber(account));
        card.setCvv2(generateCVV2());
        card.setPinCode(generatePinCode());
        card.setIssueDate(LocalDate.now());
        card.setValidTill(card.getIssueDate().plusYears(cardValidityPeriod));

        return card;
    }

    protected static String generateCardNumber(Account account) {

        return BIN_NUMBER +
                account.getCoin().code +
                account.getType().code +
                IDENTITY_DEFAULT_NUMBER;
    }

    private static String generatePinCode() {
        return String.format("%04d", new Random().nextInt(PIN_CODE_RANGE));
    }

    protected static String generateCVV2() {
        return String.format("%03d", new Random().nextInt(CVV2_RANGE));
    }
}
