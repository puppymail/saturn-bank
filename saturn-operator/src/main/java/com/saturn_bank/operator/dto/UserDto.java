package com.saturn_bank.operator.dto;

import static com.saturn_bank.operator.exception.ValidationErrorMessages.EMPTY_EMAIL_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.EMPTY_NAME_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.EMPTY_PASSWORD_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.EMPTY_PHONE_NUMBER_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.INVALID_BIRTH_DATE_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.INVALID_EMAIL_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.INVALID_ID_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.INVALID_NAME_SIZE_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.INVALID_PASSWORD_SIZE_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.INVALID_PHONE_NUMBER_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.INVALID_REG_DATE_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.INVALID_USER_ROLE_ERROR_MSG;
import static com.saturn_bank.operator.exception.ValidationErrorMessages.INVALID_USER_TYPE_ERROR_MSG;

//import com.saturn_bank.operator.constraints.Enum;
//import com.saturn_bank.operator.constraints.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @Positive(message = INVALID_ID_ERROR_MSG)
    private long id;

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
    private LocalDate birthDate;

    private LocalDateTime registrationDate;

    private LocalDateTime lastModified;

//    @Enum(value = com.saturn_bank.operator.dao.UserType.class, message = INVALID_USER_TYPE_ERROR_MSG)
    private String type;

//    @Enum(value = com.saturn_bank.operator.dao.UserRole.class, message = INVALID_USER_ROLE_ERROR_MSG)
    private String role;

    @NotEmpty(message = EMPTY_PASSWORD_ERROR_MSG)
    @Length(message = INVALID_PASSWORD_SIZE_ERROR_MSG, min = 1, max = 32)
    private String password;

    private boolean isDeleted;

}
