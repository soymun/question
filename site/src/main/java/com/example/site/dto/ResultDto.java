package com.example.site.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResultDto<T> {

    private T data;

    private List<String> errors = new ArrayList<>();
}
