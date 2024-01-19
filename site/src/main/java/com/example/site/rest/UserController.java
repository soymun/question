package com.example.site.rest;


import com.example.site.dto.ResultDto;
import com.example.site.dto.user.PasswordUpdateDto;
import com.example.site.dto.user.UserDto;
import com.example.site.dto.user.UserUpdateDto;
import com.example.site.security.UserDetailImpl;
import com.example.site.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    private ResponseEntity<ResultDto<UserDto>> updateUser(@RequestBody UserUpdateDto userUpdateDto){
        ResultDto<UserDto> result = new ResultDto<>();
        try {
            result.setData(userService.updateUser(userUpdateDto));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.getErrors().add(e.getMessage());
            return ResponseEntity.ok(result);
        }
    }

    @GetMapping("/{id}")
    private ResponseEntity<ResultDto<UserDto>> getUserById(@PathVariable Long id){
        ResultDto<UserDto> result = new ResultDto<>();
        try {
            result.setData(userService.findById(id));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.getErrors().add(e.getMessage());
            return ResponseEntity.ok(result);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    private ResponseEntity<ResultDto> deleteById(@PathVariable Long id){
        ResultDto result = new ResultDto();
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.getErrors().add(e.getMessage());
            return ResponseEntity.ok(result);
        }
    }

    @PutMapping("/change-password")
    private ResponseEntity<ResultDto> passwordUpdate(@RequestBody PasswordUpdateDto passwordUpdateDto, @AuthenticationPrincipal UserDetailImpl userDetail){
        ResultDto userDto = new ResultDto();
        try {
            if(passwordUpdateDto.getId().equals(userDetail.getId()) || userDetail.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
                if (userService.updatePassword(passwordUpdateDto)) {
                    return ResponseEntity.ok(userDto);
                }
                return ResponseEntity.status(400).build();
            } else {
                return ResponseEntity.status(403).build();
            }
        } catch (Exception e) {
            userDto.getErrors().add(e.getMessage());
            return ResponseEntity.ok(userDto);
        }
    }

    @GetMapping
    @PreAuthorize("hasAuthority('TEACHER')")
    private ResponseEntity<ResultDto<List<UserDto>>> getAll(@RequestParam int pageNumber, @RequestParam int pageSize){
        ResultDto<List<UserDto>> result = new ResultDto<>();
        try {
            result.setData(userService.getAll(pageNumber, pageSize));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.getErrors().add(e.getMessage());
            return ResponseEntity.ok(result);
        }
    }

    @GetMapping("/groups/{id}")
    @PreAuthorize("hasAuthority('TEACHER')")
    private ResponseEntity<ResultDto<List<UserDto>>> getAllByGroup(@PathVariable Long id, @RequestParam int pageNumber, @RequestParam int pageSize){
        ResultDto<List<UserDto>> result = new ResultDto<>();
        try {
            result.setData(userService.getAllByGroupId(id, pageNumber,pageSize));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.getErrors().add(e.getMessage());
            return ResponseEntity.ok(result);
        }
    }
}
