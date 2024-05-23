package com.example.site.service;

import com.example.site.dto.report.*;
import com.example.site.model.util.Role;
import io.minio.errors.*;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface ReportService {

    ReportDto saveReport(ReportCreateDto reportCreateDto);

    Resource makeReports(ReportInitDto reportInitDto) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, JRException, ClassNotFoundException;

    ReportDto updateReport(ReportUpdateDto reportUpdateDto);

    void deleteReport(Long id);

    List<ReportDto> getAllReport(Role role);

    ReportDto getById(Long id, Role role);
}
