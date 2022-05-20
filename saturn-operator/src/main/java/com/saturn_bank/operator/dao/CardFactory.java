package com.saturn_bank.operator.dao;

import static com.saturn_bank.operator.configuration.PropertiesConfig.CARD_BIN_PROP;
import static com.saturn_bank.operator.configuration.PropertiesConfig.CARD_IDENTITY_PROP;
import static com.saturn_bank.operator.configuration.PropertiesConfig.CARD_NAMESPACE;
import static com.saturn_bank.operator.configuration.PropertiesConfig.CARD_VALIDITY_PROP;
import static com.saturn_bank.operator.configuration.PropertiesConfig.CLASSPATH;
import static com.saturn_bank.operator.configuration.PropertiesConfig.DEFAULT_CARD_DATA_PROPS_FILE;
import static com.saturn_bank.operator.configuration.PropertiesConfig.DEFAULT_NAMESPACE;
import static java.time.LocalDate.now;
import static java.util.Objects.requireNonNull;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;

@Slf4j
@Component
@PropertySource(CLASSPATH + DEFAULT_CARD_DATA_PROPS_FILE)
public class CardFactory {

    private static final int PIN_CODE_RANGE = 9999;
    private static final int CVV2_RANGE = 999;

    private static String BIN_NUMBER;
    private static String IDENTITY_DEFAULT_NUMBER;
    private static int DEFAULT_VALIDITY_PERIOD;

    private final Environment env;

    @Autowired
    public CardFactory(Environment env) {
        this.env = env;
    }

    @PostConstruct
    private void init() {
        DEFAULT_VALIDITY_PERIOD = Integer.parseInt(requireNonNull(
                env.getProperty(CARD_NAMESPACE + DEFAULT_NAMESPACE + CARD_VALIDITY_PROP)));
        BIN_NUMBER = env.getProperty(CARD_NAMESPACE + DEFAULT_NAMESPACE + CARD_BIN_PROP);
        IDENTITY_DEFAULT_NUMBER = env.getProperty(CARD_NAMESPACE + DEFAULT_NAMESPACE + CARD_IDENTITY_PROP);
    }

    public Card createCard(Account account, User user) {
        return createCard(account, user, DEFAULT_VALIDITY_PERIOD);
    }

    public Card createCard(Account account, User user, int cardValidityPeriod) {
        Card card = new Card();

        card.setAccount(account);
        card.setUser(user);
        card.setNumber(generateCardNumber(account));
        card.setCvv2(generateCVV2());
        card.setPinCode(generatePinCode());
        card.setIssueDate(now());
        card.setValidTill(card.getIssueDate().plusYears(cardValidityPeriod));

        return card;
    }

    protected String generateCardNumber(Account account) {

        return BIN_NUMBER +
                account.getCoin().code +
                account.getType().code +
                IDENTITY_DEFAULT_NUMBER;
    }

    private String generatePinCode() {
        return String.format("%04d", new Random().nextInt(PIN_CODE_RANGE));
    }

    protected String generateCVV2() {
        return String.format("%03d", new Random().nextInt(CVV2_RANGE));
    }
}
