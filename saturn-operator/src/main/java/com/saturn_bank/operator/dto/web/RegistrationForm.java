package com.saturn_bank.operator.dto.web;

//import com.saturn_bank.operator.constraints.Enum;
//import com.saturn_bank.operator.constraints.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

import static com.saturn_bank.operator.exception.ValidationErrorMessages.EMPTY_BIRTH_DATE_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.EMPTY_EMAIL_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.EMPTY_NAME_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.EMPTY_PASSWORD_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.EMPTY_PHONE_NUMBER_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.INVALID_BIRTH_DATE_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.INVALID_EMAIL_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.INVALID_NAME_SIZE_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.INVALID_PASSWORD_SIZE_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.INVALID_PHONE_NUMBER_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.INVALID_USER_ROLE_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.INVALID_USER_TYPE_ERROR_MSG;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationForm {

    @NotEmpty(message = EMPTY_NAME_ERROR_MSG)
    @Length(message = INVALID_NAME_SIZE_ERROR_MSG, min = 1, max = 32)
    private String firstName;

    private String middleName;

    @NotEmpty(message = EMPTY_NAME_ERROR_MSG)
    @Length(message = INVALID_NAME_SIZE_ERROR_MSG, min = 1, max = 32)
    private String lastName;

    @NotEmpty(message = EMPTY_PHONE_NUMBER_ERROR_MSG)
//    @PhoneNumber(message = INVALID_PHONE_NUMBER_ERROR_MSG)
    private String phoneNumber;

    @NotEmpty(message = EMPTY_EMAIL_ERROR_MSG)
    @Email(message = INVALID_EMAIL_ERROR_MSG)
    private String email;

    @NotNull(message = EMPTY_BIRTH_DATE_ERROR_MSG)
    @Past(message = INVALID_BIRTH_DATE_ERROR_MSG)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

//    @Enum(value = com.saturn_bank.operator.dao.UserType.class, message = INVALID_USER_TYPE_ERROR_MSG)
    private String type;

//    @Enum(value = com.saturn_bank.operator.dao.UserRole.class, message = INVALID_USER_ROLE_ERROR_MSG)
    private String role;

    @NotEmpty(message = EMPTY_PASSWORD_ERROR_MSG)
    @Length(message = INVALID_PASSWORD_SIZE_ERROR_MSG, min = 1, max = 32)
//    @Password
    private String password;

}
