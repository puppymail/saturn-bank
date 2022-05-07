package com.saturn_bank.operator.constraints;

public class RegEx {

    public static final String PHONE_REGEX = "(?>8|7|\\+7)(?>\\d{10})";
    public static final String STRICT_PHONE_REGEX = "^" + PHONE_REGEX + "$";
    public static final String IS_EMAIL_REGEX = "^.*@[A-Za-z0-9-]+(?>\\.[A-Za-z0-9]+)*(?>\\.[A-Za-z]{2,})$";

}
