package com.example.site.dto.sandbox;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class SandboxExecuteDto {

    @NotEmpty
    private String sql;
}
