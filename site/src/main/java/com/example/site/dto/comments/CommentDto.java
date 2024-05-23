package com.example.site.dto.comments;

import com.example.site.dto.user.UserInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Комментарий")
public class CommentDto {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Пользователь")
    private UserInclude user;

    @Schema(description = "Сообщение")
    private String message;

    @Schema(description = "Дата создания")
    private LocalDateTime createTime;
}
