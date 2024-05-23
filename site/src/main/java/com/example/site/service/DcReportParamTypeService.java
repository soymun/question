package com.example.site.service;

import com.example.site.dto.dcreportparamtype.DcReportParamTypeCreateDto;
import com.example.site.dto.dcreportparamtype.DcReportParamTypeDto;

import java.util.List;

public interface DcReportParamTypeService {

    DcReportParamTypeDto create(DcReportParamTypeCreateDto dto);

    List<DcReportParamTypeDto> getAll();

    DcReportParamTypeDto update(DcReportParamTypeDto dto);

    DcReportParamTypeDto getById(Long id);

    void deleteById(Long id);
}
