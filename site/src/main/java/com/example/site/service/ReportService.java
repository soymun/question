package com.example.site.service;

import com.example.site.dto.report.ReportAllDto;
import com.example.site.dto.report.ReportCreateDto;
import com.example.site.dto.report.ReportDto;
import com.example.site.dto.report.ReportUpdateDto;
import org.springframework.core.io.Resource;

import java.util.List;

public interface ReportService {

    void saveReport(ReportCreateDto reportCreateDto);

    Resource makeReports(Long id);

    void updateReport(ReportUpdateDto reportUpdateDto);

    void deleteReport(Long id);

    List<ReportAllDto> getAllReport();

    ReportDto getById(Long id);
}
