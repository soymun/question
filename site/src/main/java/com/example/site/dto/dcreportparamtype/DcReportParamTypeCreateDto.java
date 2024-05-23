package com.example.site.dto.dcreportparamtype;

import com.example.site.model.util.DefaultType;
import lombok.Data;

@Data
public class DcReportParamTypeCreateDto {

    private String name;

    private DefaultType defaultType;
}
