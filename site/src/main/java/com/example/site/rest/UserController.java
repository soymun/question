package com.example.site.rest;

import com.example.site.dto.ResultDto;
import com.example.site.dto.user.PasswordUpdateDto;
import com.example.site.dto.user.UserDto;
import com.example.site.dto.user.UserUpdateDto;
import com.example.site.security.UserDetailImpl;
import com.example.site.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserServiceImpl userService;

    @PutMapping
    private ResponseEntity<ResultDto<UserDto>> updateUser(@RequestBody UserUpdateDto userUpdateDto) {
        return ResponseEntity.ok(new ResultDto<>(userService.updateUser(userUpdateDto)));
    }

    @GetMapping("/{id}")
    private ResponseEntity<ResultDto<UserDto>> getUserById(@PathVariable Long id) {
        log.info("" + id);
        return ResponseEntity.ok(new ResultDto<>(userService.findById(id)));
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<ResultDto<UserDto>> deleteById(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(201).build();

    }

    @PutMapping("/change-password")
    private ResponseEntity<ResultDto<UserDto>> passwordUpdate(@RequestBody PasswordUpdateDto passwordUpdateDto, @AuthenticationPrincipal(expression = "id") Long id, @AuthenticationPrincipal(expression = "grantedAuthorities") List<? extends GrantedAuthority> grantedAuthorities) {
        if (passwordUpdateDto.getId().equals(id) || grantedAuthorities.stream().anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            if (userService.updatePassword(passwordUpdateDto)) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping
    private ResponseEntity<ResultDto<List<UserDto>>> getAll(@RequestParam int pageNumber, @RequestParam int pageSize) {
        return ResponseEntity.ok(new ResultDto<>(userService.getAll(pageNumber, pageSize)));

    }

    @GetMapping("/groups/{id}")
    private ResponseEntity<ResultDto<List<UserDto>>> getAllByGroup(@PathVariable Long id, @RequestParam int pageNumber, @RequestParam int pageSize) {
        return ResponseEntity.ok(new ResultDto<>(userService.getAllByGroupId(id, pageNumber, pageSize)));
    }
}
