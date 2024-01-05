package com.example.site.dto;

import lombok.Data;

@Data
public class UserCreateDto {

    private String firstName;

    private String secondName;

    private String patronymic;

    private String email;

    private String password;

    private Boolean active;

    private Long groups;

}
