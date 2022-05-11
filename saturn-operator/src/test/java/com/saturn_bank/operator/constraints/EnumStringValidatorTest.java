package com.saturn_bank.operator.constraints;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.saturn_bank.operator.dao.UserRole;
import com.saturn_bank.operator.dao.UserType;
import com.saturn_bank.operator.dto.UserDto;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

class EnumStringValidatorTest {

    private final String ROLE_FIELD_NAME = "role";
    private final String TYPE_FIELD_NAME = "type";

    UserDto invalidUser;
    UserDto validUser;

    EnumStringValidator enumValidator;

    Validator validator;

    public EnumStringValidatorTest() {
        enumValidator = new EnumStringValidator();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void isValid_illegalRoleAndTypeProvided_validationFails() {
        UserDto invalidUser = new UserDto();
        invalidUser.setRole("invalid role");
        invalidUser.setType("invalid type");

        Set<ConstraintViolation<UserDto>> violations = validator.validate(invalidUser);
        assertEquals(2, violations.stream().filter(cv -> {
            String prop = cv.getPropertyPath().toString();
            return prop.equals(ROLE_FIELD_NAME) || prop.equals(TYPE_FIELD_NAME);
        }).count());
    }

    @Test
    void isValid_legalRoleAndTypeProvided_validationSucceeds() {
        UserDto validUser = new UserDto();
        invalidUser.setRole(UserRole.ADMIN.toString());
        invalidUser.setType(UserType.EMPLOYEE.toString());

        Set<ConstraintViolation<UserDto>> violations = validator.validate(validUser);
        assertEquals(0, violations.stream().filter(cv -> {
            String prop = cv.getPropertyPath().toString();
            return prop.equals(ROLE_FIELD_NAME) || prop.equals(TYPE_FIELD_NAME);
        }).count());
    }

}
