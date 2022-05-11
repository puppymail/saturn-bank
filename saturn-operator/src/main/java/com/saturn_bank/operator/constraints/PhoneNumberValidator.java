package com.saturn_bank.operator.constraints;

import static com.saturn_bank.operator.constraints.RegEx.STRICT_PHONE_REGEX;
import static java.util.Objects.isNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if (isNull(s)) {
            return false;
        }

        return s.trim().matches(STRICT_PHONE_REGEX);
    }

}
