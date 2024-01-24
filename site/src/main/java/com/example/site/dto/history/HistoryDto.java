package com.example.site.dto.history;

import com.example.site.dto.user.UserInclude;
import lombok.Data;

@Data
public class HistoryDto {

    private Long id;

    private TaskInclude task;

    private UserInclude user;

    private String message;

    private Boolean rights;

    private String code;
}
