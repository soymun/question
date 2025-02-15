package com.example.site.controllers;

import com.example.site.dto.ResultDto;
import com.example.site.dto.user.PasswordUpdateDto;
import com.example.site.dto.user.UserDto;
import com.example.site.dto.user.UserUpdateDto;
import com.example.site.security.UserDetailImpl;
import com.example.site.service.impl.UserServiceImpl;
import com.example.site.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "User Controller")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserServiceImpl userService;

    @PutMapping
    @Operation(description = "Изменение пользователя")
    private ResponseEntity<ResultDto<UserDto>> updateUser(@Valid @RequestBody UserUpdateDto userUpdateDto) {
        return ResponseEntity.ok(new ResultDto<>(userService.updateUser(userUpdateDto)));
    }

    @GetMapping("/{id}")
    @Operation(description = "Получение пользователя")
    private ResponseEntity<ResultDto<UserDto>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(new ResultDto<>(userService.findById(id)));
    }

    @GetMapping("/user")
    @Operation(description = "Получение пользователя")
    private ResponseEntity<ResultDto<UserDto>> getUser() {

        UserDetailImpl userDetail = SecurityUtil.getUserDetail();

        return ResponseEntity.ok(new ResultDto<>(userService.findById(userDetail.getId())));
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Удаление пользователя")
    private ResponseEntity<ResultDto<UserDto>> deleteById(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(201).build();

    }

    @PutMapping("/change-password")
    @Operation(description = "Изменение пароля")
    private ResponseEntity<ResultDto<UserDto>> passwordUpdate(@Valid @RequestBody PasswordUpdateDto passwordUpdateDto) {

        UserDetailImpl userDetail = SecurityUtil.getUserDetail();

        if (passwordUpdateDto.getId().equals(userDetail.getId()) || userDetail.isAdmin()) {
            if (userService.updatePassword(passwordUpdateDto)) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping
    @Operation(description = "Получение всех пользователей")
    private ResponseEntity<ResultDto<List<UserDto>>> getAll(@RequestParam String name) {
        return ResponseEntity.ok(new ResultDto<>(userService.getAll(name)));

    }

    @GetMapping("/groups/{id}")
    @Operation(description = "Получение пользователей по группе")
    private ResponseEntity<ResultDto<List<UserDto>>> getAllByGroup(@PathVariable Long id, @RequestParam int pageNumber, @RequestParam int pageSize) {
        return ResponseEntity.ok(new ResultDto<>(userService.getAllByGroupId(id, pageNumber, pageSize)));
    }
}
