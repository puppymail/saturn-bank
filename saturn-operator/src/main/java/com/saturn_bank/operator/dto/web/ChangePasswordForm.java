package com.saturn_bank.operator.dto.web;

import com.saturn_bank.operator.constraints.Password;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordForm {

    private String oldPassword;

    @NotEmpty(message = "{password.notEmpty}")
    @Password(message = "{password.invalid}")
    private String newPassword;

}
