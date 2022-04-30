package com.saturn_bank.operator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String birthDate;
    private String registrationDate;
    private String lastLogin;
    private String lastModified;
    private String type;
    private String role;
    private String password;
    private boolean isDeleted;

}
