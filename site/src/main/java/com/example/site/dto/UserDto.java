package com.example.site.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {

    private Long id;

    private String firstName;

    private String secondName;

    private String patronymic;

    private String email;

    private LocalDateTime firstEntry;

    private LocalDateTime lastEntry;

    private GroupDto groups;
}
