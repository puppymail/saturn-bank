package com.saturn_bank.operator.constraints;

import static com.saturn_bank.operator.constraints.RegEx.STRICT_PHONE_REGEX;
import static java.util.Objects.isNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private static final String PASSWORD_REGEX =
            "^[_A-Za-z0-9+!?()\\[\\]`;:#$%^&*=@][_A-Za-z0-9\\-+!?()\\[\\]`;:#$%^&*=@.]";
    private String pattern;

    @Override
    public void initialize(Password constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        int min = constraintAnnotation.minLength() - 1;
        if (min < 0) {
            min = 0;
        }
        int max = constraintAnnotation.maxLength() - 1;
        if (max < 1) {
            min = 1;
        }
        pattern = PASSWORD_REGEX + "{" + min + "," + max + "}$";
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if (isNull(s)) {
            return true;
        }

        return s.trim().matches(pattern);
    }

}
