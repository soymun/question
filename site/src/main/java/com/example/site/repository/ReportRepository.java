package com.example.site.repository;

import com.example.site.model.Report;
import com.example.site.model.util.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("from Report r where r.permission in (:permissions)")
    List<Report> getReportByRole(Set<Permission> permissions);
    @Query("from Report r where r.permission in (:permissions) and r.id = :id")
    Optional<Report> getReportByIdAndRole(Long id, Set<Permission> permissions);
}
