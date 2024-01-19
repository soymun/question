package com.example.site.service.impl;

import com.example.site.dto.user.*;
import com.example.site.exception.NotFoundException;
import com.example.site.mappers.UserMapper;
import com.example.site.model.Groups;
import com.example.site.model.Role;
import com.example.site.model.User;
import com.example.site.repository.UserRepository;
import com.example.site.security.UserDetailImpl;
import com.example.site.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Optional.ofNullable;

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
                    .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

            userRepository.save(findUser);
            return new UserDetailImpl(findUser.getId(), findUser.getEmail(), findUser.getPassword(), findUser.getRole().getAuthority(), findUser.getActive());
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    @Override
    public UserDto findUserByEmail(String email) {
        return userMapper.userToUserDto(
                userRepository.findUserByEmail(email)
                        .orElseThrow(() -> new NotFoundException("Пользователь не найден")));
    }

    @Override
    public UserDto saveUser(UserCreateDto userCreateDto) {
        if (userCreateDto != null) {
            User mappedUser = userMapper.userCreateDtoToUser(userCreateDto);
            mappedUser.setPassword(passwordEncoder.encode(mappedUser.getPassword()));
            mappedUser.setRole(Role.USER);
            return userMapper.userToUserDto(userRepository.save(mappedUser));
        }
        throw new NotFoundException("Пользователь не валиден");
    }

    @Override
    public UserDto updateUser(UserUpdateDto userUpdateDto) {
        if (userUpdateDto != null) {
            User user = userRepository.findById(userUpdateDto.getId())
                    .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

            ofNullable(userUpdateDto.getFirstName()).ifPresent(user::setFirstName);
            ofNullable(userUpdateDto.getSecondName()).ifPresent(user::setSecondName);
            ofNullable(userUpdateDto.getPatronymic()).ifPresent(user::setPatronymic);
            ofNullable(userUpdateDto.getActive()).ifPresent(user::setActive);
            ofNullable(userUpdateDto.getGroups()).map(Groups::new).ifPresent(user::setGroups);

            return userMapper.userToUserDto(userRepository.save(user));
        }
        throw new NotFoundException("Пользователь не валиден");
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public boolean updatePassword(PasswordUpdateDto passwordUpdateDto) {
        if (passwordUpdateDto != null) {
            User user = userRepository.findById(passwordUpdateDto.getId())
                    .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
            if (passwordEncoder.matches(passwordUpdateDto.getPrevPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(passwordUpdateDto.getNewPassword()));
                userRepository.save(user);
                return true;
            }
            throw new IllegalArgumentException("Пароли не совпадают");
        }
        throw new NotFoundException("Пользователь не валиден");
    }

    @Override
    public UserDto findById(Long id) {
        return userMapper.userToUserDto(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден")));
    }

    @Override
    public Pair<Long, Role> authorizationUser(LoginDto loginDto) {
        User user = userRepository.findUserByEmail(loginDto.getEmail())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            if (user.getFirstEntry() == null) {
                user.setFirstEntry(LocalDateTime.now());
            }
            user.setLastEntry(LocalDateTime.now());
            return Pair.of(user.getId(), user.getRole());
        }
        throw new IllegalArgumentException("Неверный пароль");
    }

    @Override
    public List<UserDto> getAll(int pageNumber, int pageSize) {
        return userRepository.findAll(PageRequest.of(pageNumber, pageSize)).get().map(userMapper::userToUserDto).toList();
    }

    @Override
    public List<UserDto> getAllByGroupId(Long id, int pageNumber, int pageSize) {
        return userRepository.findUserByGroupId(id, PageRequest.of(pageNumber, pageSize)).get().map(userMapper::userToUserDto).toList();
    }
}
