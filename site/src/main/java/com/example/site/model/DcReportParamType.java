package com.example.site.model;

import com.example.site.model.util.DefaultType;
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
@Table(name = "dc_report_param_type", schema = "courses")
public class DcReportParamType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "default_type")
    private DefaultType defaultType;
}
