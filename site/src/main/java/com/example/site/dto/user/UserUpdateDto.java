package com.example.site.dto.user;

import lombok.Data;

@Data
public class UserUpdateDto {

    private Long id;

    private String firstName;

    private String secondName;

    private String patronymic;

    private Boolean active;

    private Long groups;
}
