package com.example.site.dto.comments;

import com.example.site.dto.user.UserInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {

    private Long id;

    private UserInclude user;

    private String message;

    private LocalDateTime time;
}
