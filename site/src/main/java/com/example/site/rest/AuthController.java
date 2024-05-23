package com.example.site.rest;

import com.example.site.dto.AuthDto;
import com.example.site.dto.ResultDto;
import com.example.site.dto.user.LoginDto;
import com.example.site.dto.user.UserCreateDto;
import com.example.site.model.util.Role;
import com.example.site.security.JwtTokenProvider;
import com.example.site.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "Auth controller"
)
public class AuthController {
    private final UserServiceImpl userServiceImp;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/registration")
    @Operation(description = "Регистрация нового пользователя")
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    public ResponseEntity<ResultDto<?>> registration(@Valid @RequestBody UserCreateDto userCreateDto) {
        ResultDto<?> resultDto = new ResultDto<>();
        try {
            if (userServiceImp.findUserByEmail(userCreateDto.getEmail()) == null) {
                userServiceImp.saveUser(userCreateDto);
                return ResponseEntity.status(201).build();
            } else {
                return ResponseEntity.status(400).build();
            }
        } catch (Exception e) {
            resultDto.getErrors().add(e.getMessage());
            return ResponseEntity.ok(resultDto);
        }
    }

    @PostMapping("/login")
    @Operation(description = "Аунтификация")
    public ResponseEntity<ResultDto<AuthDto>> login(@Valid @RequestBody LoginDto loginDTO) {
        ResultDto<AuthDto> resultDto = new ResultDto<>();
        try {
            AuthDto authDto = new AuthDto();
            Pair<Long, Role> result = userServiceImp.authorizationUser(loginDTO);
            Pair<String, String> auth = jwtTokenProvider.createToken(loginDTO.getEmail(), result.getSecond());
            authDto.setId(result.getFirst());
            authDto.setRole(result.getSecond());
            authDto.setAccessToken(auth.getFirst());
            authDto.setRefreshToken(auth.getSecond());
            resultDto.setData(authDto);
            return ResponseEntity.ok(resultDto);
        } catch (Exception e) {
            resultDto.getErrors().add(e.getMessage());
            return ResponseEntity.ok(resultDto);
        }
    }

    @PostMapping("/refresh")
    @Operation(description = "Обновить токен")
    public ResponseEntity<ResultDto<?>> refresh(@RequestHeader(name = "Authorization") String refresh) {
        try {
            AuthDto authDto = new AuthDto();
            String email = jwtTokenProvider.getEmailByRefreshToken(refresh);
            Pair<Long, Role> res = userServiceImp.getUserRole(email);
            Pair<String, String> auth = jwtTokenProvider.createToken(email, res.getSecond());
            authDto.setId(res.getFirst());
            authDto.setRefreshToken(auth.getSecond());
            authDto.setAccessToken(auth.getFirst());
            authDto.setRole(res.getSecond());
            return ResponseEntity.ok(new ResultDto<>(authDto));
        } catch (Exception e) {
            ResultDto<AuthDto> resultDto = new ResultDto<>();
            resultDto.getErrors().add(e.getMessage());
            return ResponseEntity.ok(resultDto);
        }
    }
}
