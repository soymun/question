package com.example.site.model;

import com.example.site.model.util.Permission;
import com.example.site.model.util.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "reports", schema = "courses")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "file_name")
    private String fileName;

    @Enumerated(EnumType.ORDINAL)
    private Permission permission;

    @Column(name = "default_report")
    private boolean defaultReport;

    @NotNull
    private String sql;
}
