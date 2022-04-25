package com.epam.saturn.operator.supplier.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;

public class RandomData {
    final String lettersU = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    final String lettersL = "abcdefghijklmnopqrstuvwxyz";
    final String digits = "0123456789";
    final int DEFAULT_MIN_NAME_LENGTH = 2;
    final int DEFAULT_MAX_NAME_LENGTH = 15;
    final int DEFAULT_MIN_SERVER_LENGTH = 5;
    final int DEFAULT_MAX_SERVER_LENGTH = 10;
    final String DEFAULT_PHONE_START = "89";
    final int DEFAULT_PHONE_LENGTH = 11;
    final String DEFAULT_CARD_START = "4000";
    final int DEFAULT_CARD_LENGTH = 16;
    final String DEFAULT_ACCOUNT_START = "6";
    final int DEFAULT_ACCOUNT_LENGTH = 12;
    final LocalDate DEFAULT_MIN_LOCALDATE = LocalDate.of(1900, 1, 1);
    final LocalDate DEFAULT_MAX_LOCALDATE = LocalDate.of(2022, 12, 31);
    final LocalDateTime DEFAULT_MIN_LOCALDATETIME = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
    final LocalDateTime DEFAULT_MAX_LOCALDATETIME = LocalDateTime.of(2022, 12, 31, 23, 59, 59);

    public LocalDate getRandomDate(LocalDate minDate, LocalDate maxDate){
        if (minDate.isAfter(maxDate)){
            throw new IllegalArgumentException();
        }
        long minDay = minDate.toEpochDay();
        long maxDay = maxDate.toEpochDay();
        long randomDay = (long) (Math.random() * ((maxDay - minDay) + 1) + minDay);
        return LocalDate.ofEpochDay(randomDay);
    }

    public LocalDate getRandomDate(){
        return getRandomDate(DEFAULT_MIN_LOCALDATE, DEFAULT_MAX_LOCALDATE);
    }

    public LocalDateTime getRandomDateTime(LocalDateTime minDateTime, LocalDateTime maxDateTime){
        if (minDateTime.isAfter(maxDateTime)){
            throw new IllegalArgumentException();
        }
        long minTime = minDateTime.toEpochSecond(ZoneOffset.UTC);
        long maxTime = maxDateTime.toEpochSecond(ZoneOffset.UTC);
        long randomTime = (long) (Math.random() * ((maxTime - minTime) + 1) + minTime);
        return LocalDateTime.ofEpochSecond(randomTime, 0, ZoneOffset.UTC);
    }

    public LocalDateTime getRandomDateTime(){
        return getRandomDateTime(DEFAULT_MIN_LOCALDATETIME, DEFAULT_MAX_LOCALDATETIME);
    }
    
    public String getRandomString(int minSize, int maxSize){
        StringBuilder builder = new StringBuilder();
        if (minSize < 0 || maxSize < minSize){
            throw new IllegalArgumentException();
        }
        Random random = new Random();
        int size = minSize + random.nextInt(maxSize - minSize + 1);
        for (int i = 0; i < size; ++i){
            builder.append(lettersL.charAt(random.nextInt(lettersL.length())));
        }
        return builder.toString();
    }

    public String getRandomName(int minSize, int maxSize){
        if (minSize <= 0 || maxSize < minSize){
            throw new IllegalArgumentException();
        }
        Random random = new Random();
        return lettersU.charAt(random.nextInt(lettersU.length())) +
                getRandomString(minSize - 1, maxSize - 1);
    }

    public String getRandomName(){
        return getRandomName(DEFAULT_MIN_NAME_LENGTH, DEFAULT_MAX_NAME_LENGTH);
    }

    public String getRandomNumString(String start, int size){
        if (size <= 0){
            throw new IllegalArgumentException();
        }
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        builder.append(start);
        for (int i = 0; i < size - start.length(); ++i){
            builder.append(digits.charAt(random.nextInt(digits.length())));
        }
        return builder.toString();
    }

    public String getRandomNumString(String start, int minSize, int maxSize){
        Random random = new Random();
        return getRandomNumString(start, minSize + random.nextInt(maxSize - minSize + 1));
    }

    public String getRandomCVV(){
        return getRandomNumString("", 3);
    }

    public String getRandomPIN(){
        return getRandomNumString("", 4);
    }

    public String getRandomAccountNumber(){
        return getRandomNumString(DEFAULT_ACCOUNT_START, DEFAULT_ACCOUNT_LENGTH);
    }

    public String getRandomPhone(){
        return getRandomNumString(DEFAULT_PHONE_START, DEFAULT_PHONE_LENGTH);
    }

    public String getRandomCardNumber(){
        return getRandomNumString(DEFAULT_CARD_START, DEFAULT_CARD_LENGTH);

    }

    public String getRandomEmail(int minNameLength, int maxNameLength, int minCharServer, int maxCharServer){
        if (minNameLength < 0 || maxNameLength < minNameLength || minCharServer < 0 || maxCharServer < minCharServer){
            throw new IllegalArgumentException();
        }
        return getRandomString(minNameLength, maxNameLength) +
                "@" +
                getRandomString(minCharServer, maxCharServer) +
                "." +
                getRandomString(2, 3);
    }

    public String getRandomEmail(){
        return getRandomEmail(DEFAULT_MIN_NAME_LENGTH, DEFAULT_MAX_NAME_LENGTH, DEFAULT_MIN_SERVER_LENGTH, DEFAULT_MAX_SERVER_LENGTH);
    }



}
