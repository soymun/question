package com.example.site.dto.user;

import com.example.site.dto.group.GroupDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "Пользователь")
public class UserDto {

    private List<String> error = new ArrayList<>();

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Имя")
    private String firstName;

    @Schema(description = "Фамилия")
    private String secondName;

    @Schema(description = "Отчество")
    private String patronymic;

    @Schema(description = "Email")
    private String email;

    @Schema(description = "Первый заход")
    private LocalDateTime firstEntry;

    @Schema(description = "Последний заход")
    private LocalDateTime lastEntry;

    @Schema(description = "Группа")
    private GroupDto groups;
}
