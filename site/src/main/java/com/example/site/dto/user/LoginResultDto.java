package com.example.site.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Schema(description = "Вход")
public class LoginResultDto {

    @Schema(description = "Результат")
    Map<String, Object> data;

    private List<String> errors = new ArrayList<>();
}
