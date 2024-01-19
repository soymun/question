package com.example.site.dto.user;

import lombok.Data;

@Data
public class PasswordUpdateDto {

    private Long id;

    private String prevPassword;

    private String newPassword;
}
