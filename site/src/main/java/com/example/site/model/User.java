package com.example.site.model;


import com.example.site.model.util.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users", schema = "courses")
@org.hibernate.annotations.Cache(region = "user", usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Groups groups;

    @CreationTimestamp
    @Column(name = "create_time")
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    public User(Long id) {
        this.id = id;
    }
}
