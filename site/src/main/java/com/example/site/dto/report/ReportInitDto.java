package com.example.site.dto.report;

import com.example.site.service.impl.ReportServiceImpl;
import lombok.Data;

import java.util.List;

@Data
public class ReportInitDto {

    private Long reportId;

    private ReportServiceImpl.ReportOutputFormat reportOutputFormat;

    private List<ReportParamValue> reportParamValues;
}
