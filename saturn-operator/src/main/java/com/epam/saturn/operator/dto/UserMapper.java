package com.epam.saturn.operator.dto;

import com.epam.saturn.operator.dao.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User dtoToUser(UserDto userDto);

    UserDto userToDto(User user);

}
