package com.example.site.mappers;

import com.example.site.dto.dcreportparamtype.DcReportParamTypeCreateDto;
import com.example.site.dto.dcreportparamtype.DcReportParamTypeDto;
import com.example.site.model.DcReportParamType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DcReportParamTypeMapper extends DefaultMapper<DcReportParamTypeCreateDto, DcReportParamTypeDto, DcReportParamTypeDto, DcReportParamType> {

}
