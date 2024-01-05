package com.example.site.service.impl;

import com.example.site.dto.LoginDto;
import com.example.site.dto.UserCreateDto;
import com.example.site.dto.UserDto;
import com.example.site.exception.UserNotFoundException;
import com.example.site.mappers.UserMapper;
import com.example.site.model.Role;
import com.example.site.model.User;
import com.example.site.repository.UserRepository;
import com.example.site.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username != null && !username.isBlank()) {
            User findUser = userRepository.findUserByEmail(username)
                    .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

            userRepository.save(findUser);
            return new org.springframework.security.core.userdetails.User(findUser.getEmail(), findUser.getPassword(), findUser.getRole().getAuthority());
        } else {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

    @Override
    public UserDto findUserByEmail(String email) {
        return userMapper.userToUserDto(
                userRepository.findUserByEmail(email)
                        .orElseThrow(() -> new UserNotFoundException("Пользователь не найден")));
    }

    @Override
    public UserDto saveUser(UserCreateDto userCreateDto) {
        if(userCreateDto != null){
            User mappedUser = userMapper.userCreateDtoToUser(userCreateDto);
            mappedUser.setPassword(passwordEncoder.encode(mappedUser.getPassword()));
            mappedUser.setRole(Role.USER);
            return userMapper.userToUserDto(userRepository.save(mappedUser));
        }
        throw new UserNotFoundException("Пользователь не валиден");
    }

    @Override
    public Pair<Long, Role> authorizationUser(LoginDto loginDto) {
        User user = userRepository.findUserByEmail(loginDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            if(user.getFirstEntry() == null){
                user.setFirstEntry(LocalDateTime.now());
            }
            user.setLastEntry(LocalDateTime.now());
            return Pair.of(user.getId(), user.getRole());
        }
        throw new IllegalArgumentException("Неверный пароль");
    }
}
