package com.epam.saturn.operator.controller;

import com.epam.saturn.operator.dao.Account;
import com.epam.saturn.operator.dao.AccountType;
import com.epam.saturn.operator.dao.AccountCoin;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Properties;
import java.util.function.Supplier;

public class DefaultAccountSupplier implements Supplier<Account> {
    private static final String DEFAULT_ACCOUNT_DATA_PROPERTIES = "defaultAccountData.properties";

    @Override
    public Account get() {
        Account account = new Account();
        Properties accountTestData = new Properties();
        try (
                InputStream propsFileStream = getClass()
                        .getClassLoader()
                        .getResourceAsStream(DEFAULT_ACCOUNT_DATA_PROPERTIES)
        ) {
            accountTestData.load(propsFileStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        account.setNumber(accountTestData.getProperty("number"));
        account.setDefault(Boolean.parseBoolean(accountTestData.getProperty("isDefault")));
        account.setType(AccountType.valueOf(accountTestData.getProperty("type")));
        account.setPercent(new BigDecimal(accountTestData.getProperty("percent")));
        account.setAmount(new BigDecimal(accountTestData.getProperty("amount")));
        account.setCoin(AccountCoin.EUR);

        return account;

    }
}
