package com.example.site.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Courses courses;

    private Long allExecute;

    private Long rightExecute;
}
