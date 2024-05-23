package com.example.site.service.impl;

import com.example.site.dto.report.ReportInitDto;
import com.example.site.dto.report.ReportParamValue;
import com.example.site.model.DcReportParamType;
import com.example.site.model.ReportParams;
import com.example.site.repository.ReportParamRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.sql.internal.NativeQueryImpl;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportFileService {

    @PersistenceContext
    private EntityManager entityManager;

    private final ReportParamRepository reportParamRepository;

    public List<Map<String, Object>> execute(Map<String, Object> params, String sql) {
        Query query = entityManager.createNativeQuery(sql);
        NativeQueryImpl nativeQuery = (NativeQueryImpl) query;
        nativeQuery.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        for (Map.Entry<String, Object> param : params.entrySet()) {
            nativeQuery.setParameter(param.getKey(), param.getValue());
        }

        return nativeQuery.getResultList();
    }

    public Map<String, Object> getParams(ReportInitDto reportInitDto) {
        Map<String, Object> params = new HashMap<>();
        Map<String, DcReportParamType> convertParam = reportParamRepository.getAllByReport(reportInitDto.getReportId()).stream().collect(Collectors.toMap(ReportParams::getName, ReportParams::getDcReportParamType));

        for (ReportParamValue reportParamValue : reportInitDto.getReportParamValues()) {
            DcReportParamType dcReportParamType = convertParam.get(reportParamValue.getName());
            switch (dcReportParamType.getDefaultType()) {
                case LONG -> {
                    params.put(reportParamValue.getName(), Long.parseLong(reportParamValue.getValue()));
                }
                case UUID -> {
                    params.put(reportParamValue.getName(), UUID.fromString(reportParamValue.getValue()));
                }
                case LOCALDATE -> {
                    params.put(reportParamValue.getName(), LocalDate.parse(reportParamValue.getValue(), DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                }
                case LOCALDATETIME -> {
                    params.put(reportParamValue.getName(), LocalDateTime.parse(reportParamValue.getValue(), DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
                }
                default -> {
                    params.put(reportParamValue.getName(), reportParamValue.getValue());
                }
            }
        }

        return params;
    }
}
