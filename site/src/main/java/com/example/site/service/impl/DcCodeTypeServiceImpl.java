package com.example.site.service.impl;

import com.example.site.dto.dccodetype.DcCodeTypeCreateDto;
import com.example.site.dto.dccodetype.DcCodeTypeDto;
import com.example.site.exception.NotFoundException;
import com.example.site.mappers.DcCodeTypeMapper;
import com.example.site.model.DcCodeType;
import com.example.site.repository.DcCodeTypeRepository;
import com.example.site.service.DcCodeTypeService;
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
public class DcCodeTypeServiceImpl implements DcCodeTypeService {

    private final DcCodeTypeMapper dcCodeTypeMapper;

    private final DcCodeTypeRepository dcCodeTypeRepository;

    @Override
    @CachePut("dc_code_type")
    public DcCodeTypeDto create(DcCodeTypeCreateDto dto) {
        log.info("Save dc_code_type");
        return dcCodeTypeMapper.entityToDto(dcCodeTypeRepository.save(dcCodeTypeMapper.createToEntity(dto)));
    }

    @Override
    @Cacheable("dc_code_type")
    public List<DcCodeTypeDto> getAll() {
        return dcCodeTypeRepository.findAll().stream().map(dcCodeTypeMapper::entityToDto).toList();
    }

    @Override
    public DcCodeTypeDto update(DcCodeTypeDto dto) {

        log.info("Update code_type {}", dto.getId());

        DcCodeType find = dcCodeTypeRepository.findById(dto.getId()).orElseThrow(() -> new NotFoundException("Объект не найден"));

        ofNullable(dto.getName()).ifPresent(find::setName);

        return dcCodeTypeMapper.entityToDto(dcCodeTypeRepository.save(find));
    }

    @Override
    public DcCodeTypeDto getById(Long id) {
        return dcCodeTypeMapper.entityToDto(dcCodeTypeRepository.findById(id).orElseThrow(() -> new NotFoundException("Объект не найден")));
    }

    @Override
    @CacheEvict("dc_code_type")
    public void deleteById(Long id) {
        dcCodeTypeRepository.deleteById(id);
    }
}
