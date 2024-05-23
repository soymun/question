package com.example.site.service;

import com.example.site.dto.reportparam.ReportParamCreateDto;
import com.example.site.dto.reportparam.ReportParamDto;

import java.util.List;

public interface ReportParamService {

    List<ReportParamDto> getAllByReportId(Long id);

    List<ReportParamDto> getAll();

    ReportParamDto create(ReportParamCreateDto reportParamCreateDto);

    ReportParamDto update(ReportParamDto reportParamDto);

    void delete(Long id);

    ReportParamDto getById(Long id);
}
