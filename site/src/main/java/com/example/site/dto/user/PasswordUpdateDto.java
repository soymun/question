package com.example.site.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Смена пароля")
public class PasswordUpdateDto {

    @NotNull
    @Schema(description = "ID")
    private Long id;

    @NotNull
    @Schema(description = "Предыдущий пароль")
    private String prevPassword;

    @NotNull
    @Schema(description = "Новый")
    private String newPassword;
}
