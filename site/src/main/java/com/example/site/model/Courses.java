package com.example.site.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "courses", schema = "courses")
public class Courses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseName;

    private String about;

    private String pathImage;

    @Enumerated(EnumType.ORDINAL)
    private CourseType courseType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Fetch(FetchMode.JOIN)
    private User userCreated;

    private LocalDate timeCreated;

    private String schema;

    private Boolean open;

    private Boolean deleted;

    private LocalDateTime timeExecute;

    public Courses(Long id) {
        this.id = id;
    }
}
