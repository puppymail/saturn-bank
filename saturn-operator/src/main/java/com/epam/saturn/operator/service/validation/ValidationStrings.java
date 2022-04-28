package com.epam.saturn.operator.service.validation;

public class ValidationStrings {

    public static final String cardNumberRegexp= "[0-9]{16}";
    public static final String phoneNumberRegexp= "^(\\+7|7|8|9)?[\\s\\-]?\\(?[49][0-9]{2}\\)?[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{2}[\\s\\-]?[0-9]{2}$";
    public static final String accountNumberRegexp= "^\\d{20}";

}
