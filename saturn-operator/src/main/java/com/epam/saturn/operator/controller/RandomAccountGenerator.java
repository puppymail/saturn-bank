
package com.epam.saturn.operator.controller;

import com.epam.saturn.operator.dao.Account;
import com.epam.saturn.operator.dao.AccountCoin;
import com.epam.saturn.operator.dao.AccountType;
import com.epam.saturn.operator.supplier.util.RandomData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;
import java.util.function.Supplier;

@Slf4j
@Component
public class RandomAccountGenerator implements Supplier<Account> {

    static final double MIN_AMOUNT = 1000;
    static final double AMOUNT_INCREMENT = 100;
    static final int AMOUNT_MAX_INCREMENTS = 90;
    static final double PERCENT_INCREMENT = 0.01;
    static final int PERCENT_MAX_INCREMENTS = 100;

    @Override
    public Account get(){
        Account account = new Account();
        Random random = new Random();
        RandomData rd = new RandomData();
        account.setNumber(rd.getRandomAccountNumber());
        account.setDefault(true);
        account.setType(AccountType.values()[random.nextInt(AccountType.values().length)]);
        account.setPercent(BigDecimal.valueOf(random.nextInt (PERCENT_MAX_INCREMENTS) * PERCENT_INCREMENT));
        account.setAmount(BigDecimal.valueOf(MIN_AMOUNT).add(BigDecimal.valueOf(random.nextInt(AMOUNT_MAX_INCREMENTS) * AMOUNT_INCREMENT)));
        account.setCoin(AccountCoin.values()[random.nextInt(AccountCoin.values().length)]);

        return account;
    }

}
