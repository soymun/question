package com.example.site.mappers;

import com.example.site.dto.user.UserCreateDto;
import com.example.site.dto.user.UserDto;
import com.example.site.dto.user.UserUpdateDto;
import com.example.site.model.Groups;
import com.example.site.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToUserDto(User userDto);

    User userCreateDtoToUser(UserCreateDto userCreateDto);

    User userUpdateDtoToUser(UserUpdateDto userUpdateDto);

    default Groups map1(Long id){
        return new Groups(id);
    }
}
