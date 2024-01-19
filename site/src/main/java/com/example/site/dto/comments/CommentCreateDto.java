package com.example.site.dto.comments;

import lombok.Data;

@Data
public class CommentCreateDto {

    private Long task;

    private Long user;

    private String message;
}
