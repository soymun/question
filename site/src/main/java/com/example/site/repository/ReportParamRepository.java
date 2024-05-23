package com.example.site.repository;

import com.example.site.model.ReportParams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportParamRepository extends JpaRepository<ReportParams, Long> {

    @Query(value = "from ReportParams rp where rp.report.id = :reportId")
    List<ReportParams> getAllByReport(Long reportId);

    void deleteAllByReportId(Long reportId);
}
