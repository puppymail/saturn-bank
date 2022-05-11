package com.saturn_bank.operator.dto;

import com.saturn_bank.operator.constraints.Enum;
import com.saturn_bank.operator.constraints.Password;
import com.saturn_bank.operator.constraints.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private long id;

    @NotEmpty(message = "{name.notEmpty}")
    @Size(message = "{name.invalid}", min = 1, max = 32)
    private String firstName;

    @Size(message = "{name.invalid}", min = 1, max = 32)
    private String middleName;

    @NotEmpty(message = "{name.notEmpty}")
    @Size(message = "{name.invalid}", min = 1, max = 32)
    private String lastName;

    @NotEmpty(message = "{phone.notEmpty}")
    @PhoneNumber(message = "{phone.invalid")
    private String phoneNumber;

    @NotEmpty(message = "{email.notEmpty}")
    @Email(message = "{email.invalid")
    private String email;

    @Past(message = "{birthDate.invalid")
    private String birthDate;

    private String registrationDate;

    private String lastLogin;

    private String lastModified;

    @NotEmpty(message = "{type.notEmpty}")
    @Enum(value = com.saturn_bank.operator.dao.UserType.class, message = "{type.invalid}")
    private String type;

    @NotEmpty(message = "{role.notEmpty}")
    @Enum(value = com.saturn_bank.operator.dao.UserRole.class, message = "{role.invalid}")
    private String role;

    @NotEmpty(message = "{password.notEmpty}")
    @Password(message = "{password.invalid}")
    private String password;

    private boolean isDeleted;

}
