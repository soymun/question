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
    private User userCreated;

    private LocalDate timeCreated;

    private String schema;
}
