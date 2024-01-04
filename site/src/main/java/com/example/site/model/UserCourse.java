package com.example.site.model;


import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCourse {

    @EmbeddedId
    private UserCourseId userCourseId;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean closed;
}
