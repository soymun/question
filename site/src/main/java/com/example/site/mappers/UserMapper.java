package com.example.site.mappers;

import com.example.site.dto.UserCreateDto;
import com.example.site.dto.UserDto;
import com.example.site.model.Groups;
import com.example.site.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToUserDto(User userDto);

    User userCreateDtoToUser(UserCreateDto userCreateDto);

    default Groups map1(Long id){
        return new Groups(id);
    }
}
