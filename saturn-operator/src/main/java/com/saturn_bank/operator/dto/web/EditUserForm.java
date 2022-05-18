package com.saturn_bank.operator.dto.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.saturn_bank.operator.exception.ValidationErrorMessages.EMPTY_EMAIL_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.EMPTY_NAME_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.EMPTY_PASSWORD_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.EMPTY_PHONE_NUMBER_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.INVALID_BIRTH_DATE_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.INVALID_EMAIL_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.INVALID_ID_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.INVALID_NAME_SIZE_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.INVALID_PASSWORD_SIZE_ERROR_MSG;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditUserForm {

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

    @Past(message = INVALID_BIRTH_DATE_ERROR_MSG)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    //    @Enum(value = com.saturn_bank.operator.dao.UserRole.class, message = INVALID_USER_ROLE_ERROR_MSG)
    private String role;

}