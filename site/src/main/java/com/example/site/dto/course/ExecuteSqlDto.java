package com.example.site.dto.course;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Курс")
public class ExecuteSqlDto {

    @Schema(description = "ID курса")
    private Long courseId;

    @Schema(description = "SQL")
    private String userSql;
}
