package com.example.site.dto.reportparam;

import com.example.site.dto.dcreportparamtype.DcReportParamTypeDto;
import lombok.Data;

@Data
public class ReportParamCreateDto {

    private String name;

    private DcReportParamTypeDto dcReportParamType;

    private Long report;
}
