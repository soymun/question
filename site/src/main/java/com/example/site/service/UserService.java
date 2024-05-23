package com.example.site.service;

import com.example.site.dto.user.*;
import com.example.site.model.util.Role;
import org.springframework.data.util.Pair;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserDto findUserByEmail(String email);

    UserDto saveUser(UserCreateDto userCreateDto);

    Pair<Long, Role> getUserRole(String email);

    UserDto updateUser(UserUpdateDto userUpdateDto);

    void deleteUser(Long id);

    boolean updatePassword(PasswordUpdateDto passwordUpdateDto);

    UserDto findById(Long id);

    Pair<Long, Role> authorizationUser(LoginDto loginDto);

    List<UserDto> getAll(int pageNumber, int pageSize);

    List<UserDto> getAllByGroupId(Long id, int pageNumber, int pageSize);
}
