package com.saturn_bank.operator.controller;

import com.saturn_bank.operator.dao.Card;
import com.saturn_bank.operator.supplier.util.RandomData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Random;
import java.util.function.Supplier;

@Slf4j
@Component
public class RandomCardGenerator implements Supplier<Card> {

    private final long VALID_YEARS = 2L;

    @Override
    public Card get(){
        Card card = new Card();
        Random random = new Random();
        RandomData rd = new RandomData();
        card.setNumber(rd.getRandomCardNumber());
        LocalDate issued = rd.getRandomDateTime().toLocalDate();
        card.setIssueDate(issued);
        card.setValidTill(issued.plusYears(VALID_YEARS));
        card.setCvv2(rd.getRandomCVV());
        card.setPinCode(rd.getRandomPIN());

        return card;
    }

}
