package com.example.site.model;

import com.example.site.model.util.TaskType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

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

    private String file;

    @Enumerated(value = EnumType.ORDINAL)
    private TaskType taskType;

    @ManyToOne
    @JoinColumn(name = "task_group_id", referencedColumnName = "id")
    private TaskGroup taskGroup;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @Fetch(FetchMode.JOIN)
    private Courses courses;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskInfoCode> taskInfoCode;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskInfoQuestionBox> taskInfoQuestionBox;

    @OneToOne(mappedBy = "task", cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    private TaskInfoQuestionText taskInfoQuestionText;

    @OneToOne(mappedBy = "task", cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    private TaskInfoSql taskInfoSql;

    @Column(name = "all_attempt")
    private Long allAttempt;

    @Column(name = "right_attempt")
    private Long rightAttempt;

    private boolean deleted;

    private boolean open;

    public Task(Long id) {
        this.id = id;
    }
}
