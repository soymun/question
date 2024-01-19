package com.example.site.dto.user;

import com.example.site.dto.group.GroupDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserDto {

    private List<String> error = new ArrayList<>();

    private Long id;

    private String firstName;

    private String secondName;

    private String patronymic;

    private String email;

    private LocalDateTime firstEntry;

    private LocalDateTime lastEntry;

    private GroupDto groups;
}
