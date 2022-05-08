package com.saturn_bank.operator.dto.mapper;

import com.saturn_bank.operator.dao.User;
import com.saturn_bank.operator.dto.UserDto;
import com.saturn_bank.operator.dto.web.RegistrationForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "accountList", ignore = true)
    @Mapping(target = "isDeleted", constant = "false")
    User dtoToUser(UserDto userDto);

    UserDto userToDto(User user);

    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accountList", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    User registrationFormToUser(RegistrationForm form);

}
