package com.example.site.dto.comments;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Создание комментария")
public class CommentCreateDto {

    @NotNull
    @Schema(description = "Задача")
    private Long task;

    @NotNull
    @Schema(description = "Пользователь")
    private Long user;

    @NotNull
    @Schema(description = "Сообщение")
    private String message;
}
