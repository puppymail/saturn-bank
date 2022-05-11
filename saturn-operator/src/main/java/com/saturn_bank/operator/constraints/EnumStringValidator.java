package com.saturn_bank.operator.constraints;

import static java.util.Objects.isNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumStringValidator implements ConstraintValidator<Enum, String> {

    private Object[] enums = null;

    @Override
    public void initialize(Enum constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        Class<?> aEnum = constraintAnnotation.value();
        enums = aEnum.getEnumConstants();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if (isNull(enums) || isNull(s)) {
            return false;
        }
        for (Object o : enums) {
            if (s.trim().toUpperCase().equals(o.toString())) {
                return true;
            }
        }

        return false;
    }

}
