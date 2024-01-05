package com.example.site.model;


import jakarta.persistence.*;
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
@Table(name = "user_course", schema = "courses")
public class UserCourse {

    @EmbeddedId
    private UserCourseId userCourseId;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean closed;

    @ManyToOne
    @JoinColumn(name = "mark_id")
    private CourseMarks courseMarks;
}
