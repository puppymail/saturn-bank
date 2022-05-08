package com.saturn_bank.operator.dto.web;

import static com.saturn_bank.operator.exception.ValidationErrorMessages.EMPTY_EMAIL_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.EMPTY_PASSWORD_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.INVALID_EMAIL_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.INVALID_PASSWORD_SIZE_ERROR_MSG;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm {

    @NotEmpty()
    private String login;

    @NotEmpty()
    @Length(min = 1, max = 32)
//    @Password
    private String password;

}
