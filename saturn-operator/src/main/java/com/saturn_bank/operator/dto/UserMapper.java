package com.saturn_bank.operator.dto;

import com.saturn_bank.operator.dao.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User dtoToUser(UserDto userDto);

    UserDto userToDto(User user);

}
