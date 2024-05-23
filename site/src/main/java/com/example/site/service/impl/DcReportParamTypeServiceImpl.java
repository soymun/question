package com.example.site.service.impl;

import com.example.site.dto.dcreportparamtype.DcReportParamTypeCreateDto;
import com.example.site.dto.dcreportparamtype.DcReportParamTypeDto;
import com.example.site.exception.NotFoundException;
import com.example.site.mappers.DcReportParamTypeMapper;
import com.example.site.model.DcReportParamType;
import com.example.site.repository.DcReportParamTypeRepository;
import com.example.site.service.DcReportParamTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DcReportParamTypeServiceImpl implements DcReportParamTypeService {

    private final DcReportParamTypeMapper dcReportParamTypeMapper;

    private final DcReportParamTypeRepository dcReportParamTypeRepository;

    @Override
    @CachePut("dc_report_param_type")
    public DcReportParamTypeDto create(DcReportParamTypeCreateDto dto) {
        log.info("Save dc_report_param_type");
        return dcReportParamTypeMapper.entityToDto(dcReportParamTypeRepository.save(dcReportParamTypeMapper.createToEntity(dto)));
    }

    @Override
    @Cacheable("dc_report_param_type")
    public List<DcReportParamTypeDto> getAll() {
        return dcReportParamTypeRepository.findAll().stream().map(dcReportParamTypeMapper::entityToDto).toList();
    }

    @Override
    public DcReportParamTypeDto update(DcReportParamTypeDto dto) {

        log.info("Update dc_report_param_type {}", dto.getId());

        DcReportParamType find = dcReportParamTypeRepository.findById(dto.getId()).orElseThrow(() -> new NotFoundException("Объект не найден"));

        ofNullable(dto.getName()).ifPresent(find::setName);
        ofNullable(dto.getDefaultType()).ifPresent(find::setDefaultType);

        return dcReportParamTypeMapper.entityToDto(dcReportParamTypeRepository.save(find));
    }

    @Override
    public DcReportParamTypeDto getById(Long id) {
        return dcReportParamTypeMapper.entityToDto(dcReportParamTypeRepository.findById(id).orElseThrow(() -> new NotFoundException("Объект не найден")));
    }

    @Override
    @CacheEvict("dc_report_param_type")
    public void deleteById(Long id) {
        dcReportParamTypeRepository.deleteById(id);
    }
}
