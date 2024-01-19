package com.example.site.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users", schema = "courses")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String secondName;

    private String patronymic;

    private String email;

    private String password;

    @Enumerated(value = EnumType.ORDINAL)
    private Role role;

    private Boolean active;

    private LocalDateTime firstEntry;

    private LocalDateTime lastEntry;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @Fetch(value = FetchMode.JOIN)
    private Groups groups;

    public User(Long id) {
        this.id = id;
    }
}
