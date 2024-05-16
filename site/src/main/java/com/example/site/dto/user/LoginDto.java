package com.example.site.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Вход")
public class LoginDto {

    @NotNull
    @Schema(description = "email")
    private String email;

    @NotNull
    @Schema(description = "Пароль")
    private String password;
}
