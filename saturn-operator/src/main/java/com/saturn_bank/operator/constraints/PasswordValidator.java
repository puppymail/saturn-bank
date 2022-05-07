package com.saturn_bank.operator.constraints;

import static com.saturn_bank.operator.constraints.RegEx.STRICT_PHONE_REGEX;
import static java.util.Objects.isNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if (isNull(s)) {
            return true;
        }


        return s.trim().matches(STRICT_PHONE_REGEX);
    }

}
