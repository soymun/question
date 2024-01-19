package com.example.site.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "user_task", schema = "courses")
public class UserTask {

    @EmbeddedId
    private UserTaskId userTaskId;

    private Boolean rights;

    private Boolean closed;

    private Long attempt;

    public UserTask(UserTaskId userTaskId) {
        this.userTaskId = userTaskId;
    }
}
