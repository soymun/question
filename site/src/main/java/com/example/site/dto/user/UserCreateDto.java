package com.example.site.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Создание пользователя")
public class UserCreateDto {

    @Schema(description = "Имя")
    private String firstName;

    @Schema(description = "Фамилия")
    private String secondName;

    @Schema(description = "Отчество")
    private String patronymic;

    @NotNull
    @Schema(description = "Email")
    private String email;

    @NotNull
    @Schema(description = "Пароль")
    private String password;

    @Schema(description = "Активный")
    private Boolean active;

    @NotNull
    @Schema(description = "Группа")
    private Long groups;

}
