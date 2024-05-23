package com.example.site.mappers;

import com.example.site.dto.report.ReportCreateDto;
import com.example.site.dto.report.ReportDto;
import com.example.site.dto.report.ReportUpdateDto;
import com.example.site.model.Report;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReportMapper extends DefaultMapper<ReportCreateDto, ReportUpdateDto, ReportDto, Report> {
}
