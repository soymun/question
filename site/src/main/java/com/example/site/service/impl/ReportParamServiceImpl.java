package com.example.site.service.impl;

import com.example.site.dto.reportparam.ReportParamCreateDto;
import com.example.site.dto.reportparam.ReportParamDto;
import com.example.site.exception.NotFoundException;
import com.example.site.mappers.ReportParamMapper;
import com.example.site.model.ReportParams;
import com.example.site.repository.ReportParamRepository;
import com.example.site.service.ReportParamService;
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
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReportParamServiceImpl implements ReportParamService {

    private final ReportParamRepository reportParamRepository;

    private final ReportParamMapper reportParamMapper;

    @Override
    @Cacheable("report_param")
    public List<ReportParamDto> getAllByReportId(Long id) {
        return reportParamRepository.getAllByReport(id).stream().map(reportParamMapper::entityToDto).toList();
    }

    @Override
    @Cacheable("report_param")
    public List<ReportParamDto> getAll() {
        return reportParamRepository.findAll().stream().map(reportParamMapper::entityToDto).toList();
    }

    @Override
    @CachePut("report_param")
    public ReportParamDto create(ReportParamCreateDto reportParamCreateDto) {
        log.info("Save report param");
        return reportParamMapper.entityToDto(reportParamRepository.save(reportParamMapper.createToEntity(reportParamCreateDto)));
    }

    @Override
    @CachePut("report_param")
    public ReportParamDto update(ReportParamDto reportParamDto) {

        log.info("Update report params with id {}", reportParamDto.getId());

        ReportParams reportParamsUpdate = reportParamMapper.updateToEntity(reportParamDto);
        ReportParams reportParams = reportParamRepository.findById(reportParamDto.getId()).orElseThrow(()-> new NotFoundException("Объект не найден"));

        ofNullable(reportParamDto.getName()).ifPresent(reportParams::setName);
        ofNullable(reportParamsUpdate.getDcReportParamType()).ifPresent(reportParams::setDcReportParamType);

        return reportParamMapper.entityToDto(reportParamRepository.save(reportParams));
    }

    @Override
    @CacheEvict("report_param")
    public void delete(Long id) {
        log.info("Delete report param with id {}", id);
        reportParamRepository.deleteById(id);
    }

    @Override
    public ReportParamDto getById(Long id) {
        return reportParamMapper.entityToDto(reportParamRepository.findById(id).orElseThrow(() -> new NotFoundException("Объект не найден")));
    }
}
