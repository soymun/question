package com.example.site.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserTask {

    @EmbeddedId
    private UserTaskId userTaskId;

    private Boolean right;

    private Boolean closed;

    private Long attempt;
}
