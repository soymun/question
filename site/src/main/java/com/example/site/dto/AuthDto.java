package com.example.site.dto;

import com.example.site.model.Role;
import lombok.Data;

@Data
public class AuthDto {

    private Long id;

    private String accessToken;

    private String refreshToken;

    private Role role;
}
