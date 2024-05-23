package com.example.site.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResultDto<T> {

    @Schema(description = "Значение")
    private T data;

    private List<String> errors = new ArrayList<>();

    public ResultDto(T data) {
        this.data = data;
    }

    public ResultDto() {
    }
}
