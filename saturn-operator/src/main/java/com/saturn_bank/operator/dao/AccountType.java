package com.saturn_bank.operator.dao;

public enum AccountType {
    REGULAR("17"),
    DEMAND_DEPOSITS("01");
    public String code;
    AccountType(String code) {this.code = code;}
}
