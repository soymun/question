package com.example.site.model;


import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserCourseId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Courses courses;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCourseId that = (UserCourseId) o;
        return Objects.equals(user.getId(), that.user.getId()) && Objects.equals(courses.getId(), that.courses.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getId(), courses.getId());
    }

    public UserCourseId(Long user, Long courses) {
        this.user = new User(user);
        this.courses = new Courses(courses);
    }
}
