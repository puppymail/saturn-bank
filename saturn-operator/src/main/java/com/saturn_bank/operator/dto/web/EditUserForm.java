package com.saturn_bank.operator.dto.web;

import com.saturn_bank.operator.constraints.Enum;
import com.saturn_bank.operator.constraints.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditUserForm {

    @NotEmpty(message = "{name.notEmpty}")
    @Size(message = "{name.invalid}", min = 1, max = 32)
    private String firstName;

    @Size(message = "{name.invalid}", max = 32)
    private String middleName;

    @NotEmpty(message = "{name.notEmpty}")
    @Size(message = "{name.invalid}", min = 1, max = 32)
    private String lastName;

    @NotEmpty(message = "{phone.notEmpty}")
    @PhoneNumber(message = "{phone.invalid}")
    private String phoneNumber;

    @NotEmpty(message = "{email.notEmpty}")
    @Email(message = "{email.invalid}")
    private String email;

    @NotNull(message = "{birthDate.notEmpty}")
    @Past(message = "{birthDate.invalid}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotEmpty(message = "{role.notEmpty}")
    @Enum(value = com.saturn_bank.operator.dao.UserRole.class, message = "{role.invalid}")
    private String role;

}
