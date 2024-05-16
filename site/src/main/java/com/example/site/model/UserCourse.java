package com.example.site.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_course", schema = "courses")
public class UserCourse {

    @EmbeddedId
    private UserCourseId userCourseId;

    private LocalDateTime startDate;

    private Boolean closed;

    private Boolean deleted;

    @ManyToOne
    @JoinColumn(name = "mark_id")
    private CourseMarks courseMarks;

    public UserCourse(UserCourseId userCourseId) {
        this.userCourseId = userCourseId;
    }
}
