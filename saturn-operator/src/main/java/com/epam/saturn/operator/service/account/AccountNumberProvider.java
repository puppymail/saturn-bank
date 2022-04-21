package com.epam.saturn.operator.service.account;

import com.epam.saturn.operator.dao.AccountCoin;
import com.epam.saturn.operator.dao.AccountType;

public class AccountNumberProvider {

    private static final String DEFAULT_ACCOUNT_NUMBER = "00000000000000000000";
    private static final String OFFICE_ID = "1000";
    private static final int MAX_ACCOUNT_PERSONAL_NUMBER_LENGTH = 7;
    private static final Long MAX_ACCOUNT_PERSONAL_NUMBER = (long)Math.pow(10, MAX_ACCOUNT_PERSONAL_NUMBER_LENGTH) - 1;

    enum ClientType {
        INDIVIDUAL_PERSON("408"),
        PRIVATE_LEGAL_ENTITY("407"),
        GOVERNMENT_LEGAL_ENTITY("406"),
        GOVERNMENT_AGENCIES_DEPOSIT("419"),
        RESIDENT_INDIVIDUAL_DEPOSIT("423"),
        FOREIGN_COMPANIES_FUNDS("424"),
        NON_RESIDENT_INDIVIDUAL_DEPOSIT("425");
        String code;
        ClientType(String code) {
            this.code = code;
        }
    }

    public static String getDefaultAccountNumber() {
        return DEFAULT_ACCOUNT_NUMBER;
    }

    public static String getAccountNumber(Long id, AccountType type, AccountCoin coin) {
        final int checkValue = getCheckValue(ClientType.INDIVIDUAL_PERSON.code, type.code, coin.code);
        return ClientType.INDIVIDUAL_PERSON.code
                + type.code
                + coin.code
                + checkValue
                + getOfficeId()
                + getPersonalGeneratedAccountNumber(id);
    }

    private static int getCheckValue(String code, String type, String coin) {
        //TODO: impl real-world check function
        return (Integer.parseInt(code) + Integer.parseInt(type) + Integer.parseInt(coin)) % 10;
    }

    private static String getAccountNumberCodePart(AccountType type, AccountCoin coin) {
        return ClientType.INDIVIDUAL_PERSON.code
                + type.code
                + coin.code;
    }

    private static String getOfficeId() {
        return OFFICE_ID;
    }

    //TODO 1: use MAX_ACCOUNT_PERSONAL_NUMBER_LENGTH to generate string
    static final private String ZEROS = "0000000000000000000000000000000000000";

    private static String getPersonalGeneratedAccountNumber(Long id) {

        //TODO: for security reasons add number generating, not directly using Account ID.
        if (id > MAX_ACCOUNT_PERSONAL_NUMBER) {
            throw new IllegalArgumentException("Ran out of account numbers.");
        }

        final String sid = id.toString();
        final int zerosCount = MAX_ACCOUNT_PERSONAL_NUMBER_LENGTH - sid.length();
        if (zerosCount == 0) {
            return sid;
        }
        return sid + ZEROS.substring(0, zerosCount);
    }
}