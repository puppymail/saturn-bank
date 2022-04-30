package com.saturn_bank.operator.dao;

public enum AccountCoin {
    RUB("RUB", "810"), EUR("EUR", "978"), USD("USD", "840");
    public String currency;
    public String code;
    AccountCoin(String currency, String code) {
        this.currency = currency;
        this.code = code;
    }

}
