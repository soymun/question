package com.example.site.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class LoginResultDto {

    Map<String, Object> data;

    private List<String> errors = new ArrayList<>();
}
