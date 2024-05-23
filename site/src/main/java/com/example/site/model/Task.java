package com.example.site.model;

import com.example.site.model.util.TaskType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "task", schema = "courses")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long number;

    private String name;

    private String description;

    private String title;

    @Enumerated(value = EnumType.ORDINAL)
    private TaskType taskType;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @Fetch(FetchMode.JOIN)
    private Courses courses;

    @Column(name = "all_attempt")
    private Long allAttempt;

    @Column(name = "right_attempt")
    private Long rightAttempt;

    private Boolean deleted;

    private Boolean open;
    public Task(Long id) {
        this.id = id;
    }
}
