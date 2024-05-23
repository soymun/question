package com.example.site.mappers;

import com.example.site.dto.dccodetype.DcCodeTypeCreateDto;
import com.example.site.dto.dccodetype.DcCodeTypeDto;
import com.example.site.model.DcCodeType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DcCodeTypeMapper extends DefaultMapper<DcCodeTypeCreateDto, DcCodeTypeDto, DcCodeTypeDto, DcCodeType> {
}
