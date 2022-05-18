package com.saturn_bank.operator.constraints;

import static java.util.Objects.isNull;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

import com.google.common.base.Joiner;
import org.passay.PasswordData;
import org.passay.RuleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@Scope(SCOPE_PROTOTYPE)
public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Autowired
    private org.passay.PasswordValidator validator;

    @Override
    public boolean isValid(String pass, ConstraintValidatorContext context) {
        if (isNull(pass)) {
            return false;
        }
        RuleResult result = validator.validate(new PasswordData(pass));
        if (result.isValid()) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        for (String violationMessage : validator.getMessages(result)) {
            context.buildConstraintViolationWithTemplate(violationMessage)
                    .addConstraintViolation();
        }

        return false;
    }

}
