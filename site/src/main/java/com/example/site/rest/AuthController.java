package com.example.site.rest;

import com.example.site.dto.ResultDto;
import com.example.site.dto.user.LoginDto;
import com.example.site.dto.user.UserCreateDto;
import com.example.site.model.Role;
import com.example.site.security.JwtTokenProvider;
import com.example.site.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserServiceImpl userServiceImp;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/registration")
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    public ResponseEntity<ResultDto<?>> registration(@RequestBody UserCreateDto userCreateDto) {
        ResultDto<?> resultDto = new ResultDto<>();
        try {
            if (userServiceImp.findUserByEmail(userCreateDto.getEmail()) == null) {
                userServiceImp.saveUser(userCreateDto);
                return ResponseEntity.status(201).build();
            } else {
                return ResponseEntity.status(400).build();
            }
        } catch (Exception e){
            resultDto.getErrors().add(e.getMessage());
            return ResponseEntity.ok(resultDto);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResultDto<Map<String, Object>>> login(@RequestBody LoginDto loginDTO) {
        ResultDto<Map<String, Object>> resultDto = new ResultDto<>();
        try {
            Pair<Long, Role> result = userServiceImp.authorizationUser(loginDTO);
            resultDto.setData(Map.of(
                    "id", result.getFirst(),
                    "role", result.getSecond(),
                    "token", jwtTokenProvider.createToken(loginDTO.getEmail(), result.getSecond())));
            return ResponseEntity.ok(resultDto);
        } catch (Exception e) {
            resultDto.getErrors().add(e.getMessage());
            return ResponseEntity.ok(resultDto);
        }
    }
}
