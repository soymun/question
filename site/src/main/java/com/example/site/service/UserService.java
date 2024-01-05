package com.example.site.service;

import com.example.site.dto.LoginDto;
import com.example.site.dto.UserCreateDto;
import com.example.site.dto.UserDto;
import com.example.site.model.Role;
import org.springframework.data.util.Pair;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Map;

public interface UserService extends UserDetailsService {

    UserDto findUserByEmail(String email);

    UserDto saveUser(UserCreateDto userCreateDto);

    Pair<Long, Role> authorizationUser(LoginDto loginDto);
}
