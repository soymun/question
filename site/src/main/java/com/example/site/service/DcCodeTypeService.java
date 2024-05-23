package com.example.site.service;

import com.example.site.dto.dccodetype.DcCodeTypeCreateDto;
import com.example.site.dto.dccodetype.DcCodeTypeDto;

import java.util.List;

public interface DcCodeTypeService {

    DcCodeTypeDto create(DcCodeTypeCreateDto dto);

    List<DcCodeTypeDto> getAll();

    DcCodeTypeDto update(DcCodeTypeDto dto);

    DcCodeTypeDto getById(Long id);

    void deleteById(Long id);
}
