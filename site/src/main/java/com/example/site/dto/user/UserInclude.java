package com.example.site.dto.user;

import lombok.Data;

@Data
public class UserInclude {

    private Long id;

    private String firstName;

    private String secondName;

    private String patronymic;
}
