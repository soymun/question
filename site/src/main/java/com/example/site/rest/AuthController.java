package com.example.site.rest;

import com.example.site.dto.LoginDto;
import com.example.site.dto.LoginResultDto;
import com.example.site.dto.UserCreateDto;
import com.example.site.model.Role;
import com.example.site.security.JwtTokenProvider;
import com.example.site.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class AuthController {
    private final UserServiceImpl userServiceImp;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UserCreateDto userCreateDto) {
        if (userServiceImp.findUserByEmail(userCreateDto.getEmail()) != null) {
            userServiceImp.saveUser(userCreateDto);
            return ResponseEntity.status(201).build();
        } else {
            return ResponseEntity.status(400).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDTO) {
        try {
            LoginResultDto loginResultDto = new LoginResultDto();
            Pair<Long, Role> result = userServiceImp.authorizationUser(loginDTO);
            loginResultDto.setData(Map.of(
                    "id", result.getFirst(),
                    "role", result.getSecond(),
                    "token", jwtTokenProvider.createToken(loginDTO.getEmail(), result.getSecond())));
            return ResponseEntity.ok(loginResultDto);
        } catch (Exception e) {
            LoginResultDto loginResultDto = new LoginResultDto();
            loginResultDto.getErrors().add(e.getMessage());
            return ResponseEntity.ok(loginResultDto);
        }
    }

}
