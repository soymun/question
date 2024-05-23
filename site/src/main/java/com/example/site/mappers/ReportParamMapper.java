package com.example.site.mappers;

import com.example.site.dto.reportparam.ReportParamCreateDto;
import com.example.site.dto.reportparam.ReportParamDto;
import com.example.site.model.Report;
import com.example.site.model.ReportParams;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReportParamMapper extends DefaultMapper<ReportParamCreateDto, ReportParamDto, ReportParamDto, ReportParams> {
    default Report map(Long id){
        Report report = new Report();
        report.setId(id);
        return report;
    }
}
