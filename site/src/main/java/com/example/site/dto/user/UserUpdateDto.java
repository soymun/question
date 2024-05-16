package com.example.site.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Пользователь")
public class UserUpdateDto {

    @NotNull
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Имя")
    private String firstName;

    @Schema(description = "Фамилия")
    private String secondName;

    @Schema(description = "Отчество")
    private String patronymic;

    @Schema(description = "Активный")
    private Boolean active;

    @Schema(description = "Группа")
    private Long groups;
}
