package com.saturn_bank.operator.exception;

public class ValidationErrorMessages {

    public static final String INVALID_ID_ERROR_MSG = "ID should more than 0.";
    public static final String EMPTY_NAME_ERROR_MSG = "Name cannot be empty!";
    public static final String INVALID_NAME_SIZE_ERROR_MSG = "Name should be between 1 and 32 characters!";

    public static final String EMPTY_PHONE_NUMBER_ERROR_MSG = "Phone number cannot be empty!";
    public static final String INVALID_PHONE_NUMBER_ERROR_MSG = "Phone number should only contain 11 " +
            "digits and optionally plus in the beginning";
    public static final String EMPTY_EMAIL_ERROR_MSG = "Email cannot be empty!";
    public static final String INVALID_EMAIL_ERROR_MSG = "Invalid email provided.";
    public static final String INVALID_BIRTH_DATE_ERROR_MSG = "Birth date should be in the past!";
    public static final String INVALID_REG_DATE_ERROR_MSG = "Invalid registration date.";
    public static final String INVALID_USER_ROLE_ERROR_MSG = "Invalid user role.";
    public static final String INVALID_USER_TYPE_ERROR_MSG = "Invalid user type.";
    public static final String EMPTY_PASSWORD_ERROR_MSG = "Password cannot be empty!";
    public static final String INVALID_PASSWORD_SIZE_ERROR_MSG = "Password should be between .";

}
