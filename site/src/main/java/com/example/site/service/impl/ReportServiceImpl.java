package com.example.site.service.impl;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.Page;
import com.example.site.dto.report.ReportCreateDto;
import com.example.site.dto.report.ReportDto;
import com.example.site.dto.report.ReportInitDto;
import com.example.site.dto.report.ReportUpdateDto;
import com.example.site.exception.NotFoundException;
import com.example.site.mappers.ReportMapper;
import com.example.site.model.Report;
import com.example.site.model.util.Role;
import com.example.site.repository.ReportParamRepository;
import com.example.site.repository.ReportRepository;
import com.example.site.service.ReportService;
import com.example.site.util.BucketUtil;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final ReportFileService reportFileService;

    private final ReportRepository reportRepository;

    private final ReportMapper reportMapper;

    private final ReportParamRepository reportParamRepository;

    private final MinioClient minioClient;


    @Override
    @Transactional
    @CachePut("report")
    public ReportDto saveReport(ReportCreateDto reportCreateDto) {
        log.info("Report create");

        check(reportCreateDto);

        return reportMapper.entityToDto(reportRepository.save(reportMapper.createToEntity(reportCreateDto)));
    }

    @Override
    public Resource makeReports(ReportInitDto reportInitDto) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, JRException, ClassNotFoundException {

        log.info("Generate report with id {}", reportInitDto.getReportId());

        Report report = reportRepository.findById(reportInitDto.getReportId()).orElseThrow(() -> new NotFoundException("Объект не найден"));
        List<Map<String, Object>> value = reportFileService.execute(reportFileService.getParams(reportInitDto), report.getSql());
        Map<String, Object> parameters = new HashMap<>();
        JasperExportManager jasperExportManager = JasperExportManager.getInstance(new SimpleJasperReportsContext());
        File file = File.createTempFile("report", "." + reportInitDto.getReportOutputFormat().extension);
        file.deleteOnExit();
        JasperPrint jasperReport;

        if (report.isDefaultReport()){
            FastReportBuilder fastReportBuilder = new FastReportBuilder();

            Page page = Page.Page_A4_Landscape();

            Style style = new Style();
            Font font = new Font(9, "DejaVu Serif", "DejaVu Serif", Font.PDF_ENCODING_CP1251_Cyrillic, true);
            style.setFont(font);

            if(!value.isEmpty()){
                Map<String, Object> columns = value.get(0);
                for (Map.Entry<String, Object> val: columns.entrySet()){
                    fastReportBuilder.addColumn(val.getKey(), val.getKey(), String.class, 30);
                }
            } else {
                throw new IllegalArgumentException("Ошибка выполнения sql");
            }

            fastReportBuilder
                    .setSubtitle(report.getName())
                    .setDefaultStyles(style, style, style, style)
                    .setPrintBackgroundOnOddRows(true)
                    .setUseFullPageWidth(true)
                    .setPageSizeAndOrientation(page)
                    .setReportName(report.getName());

            JRDataSource ds = new JRBeanCollectionDataSource(value);

            DynamicReport dynamicReport = fastReportBuilder.build();

            jasperReport = DynamicJasperHelper.generateJasperPrint(dynamicReport, new ClassicLayoutManager(), ds);


        } else {
            JRDataSource ds = new JRBeanCollectionDataSource(value);

            parameters.put("database", ds);

            jasperReport = JasperFillManager.fillReport((JasperReport) JRLoader.loadObject(minioClient.getObject(GetObjectArgs.builder().bucket(BucketUtil.Buckets.REPORTS.value).object(report.getName()).build())), parameters, ds);
        }

        generateReport(reportInitDto.getReportOutputFormat(), jasperExportManager, jasperReport, file);

        return new FileSystemResource(file);
    }

    @Override
    @Transactional
    @CachePut("report")
    public ReportDto updateReport(ReportUpdateDto reportUpdateDto) {

        log.info("Update report with id {}", reportUpdateDto.getId());

        Report report = reportRepository.findById(reportUpdateDto.getId()).orElseThrow(() -> new NotFoundException("Объект не найден"));

        ofNullable(reportUpdateDto.getFileName()).ifPresent(report::setFileName);
        ofNullable(reportUpdateDto.getName()).ifPresent(report::setName);
        ofNullable(reportUpdateDto.getSql()).ifPresent(sql1 -> {
            String sql = sql1.toLowerCase();
            if (!sql.contains("select") || sql.contains("insert") || sql.contains("delete") || sql.contains("alter") || sql.contains("drop") || sql.contains("update")) {
                throw new IllegalArgumentException("Неправильный sql");
            }

            report.setSql(sql1);
        });
        ofNullable(reportUpdateDto.getPermission()).ifPresent(report::setPermission);

        return reportMapper.entityToDto(reportRepository.save(report));
    }

    @Override
    @Transactional
    @CacheEvict("report")
    public void deleteReport(Long id) {
        log.info("Report delete with id {}", id);
        reportParamRepository.deleteAllByReportId(id);
        reportRepository.deleteById(id);
    }

    @Override
    @Transactional
    @Cacheable("report")
    public List<ReportDto> getAllReport(Role role) {
        return reportRepository.getReportByRole(role.getPermission()).stream().map(reportMapper::entityToDto).toList();
    }

    @Override
    @Transactional
    public ReportDto getById(Long id, Role role) {
        return reportMapper.entityToDto(reportRepository.getReportByIdAndRole(id, role.getPermission()).orElseThrow(() -> new NotFoundException("Объект не найден")));
    }


    private static void check(ReportCreateDto reportCreateDto) {
        if (reportCreateDto.getSql() != null) {
            String sql = reportCreateDto.getSql().toLowerCase();
            if (!sql.contains("select") || sql.contains("insert") || sql.contains("delete") || sql.contains("alter") || sql.contains("drop") || sql.contains("update")) {
                throw new IllegalArgumentException("Неправильный sql");
            }
        }
    }

    private void generateReport(ReportOutputFormat reportOutputFormat, JasperExportManager jasperExportManager, JasperPrint finalReport, File reportResultFile) throws JRException {
        //Генерация html отчёта
        if (reportOutputFormat == ReportOutputFormat.HTML) {
            jasperExportManager.exportToHtmlFile(finalReport, reportResultFile.getAbsolutePath());
        }
        //Генерация xlsx отчёта
        else if (reportOutputFormat == ReportOutputFormat.XLSX) {
            JRXlsxExporter pdfExporter = new JRXlsxExporter();
            pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(reportResultFile));
            pdfExporter.setExporterInput(new SimpleExporterInput(finalReport));
            pdfExporter.exportReport();
        }
        //Генерация pdf отчёта
        else if (reportOutputFormat == ReportOutputFormat.PDF) {
            jasperExportManager.exportToPdfFile(finalReport, reportResultFile.getAbsolutePath());

        } else if (reportOutputFormat == ReportOutputFormat.DOCX) {
            JRDocxExporter export = new JRDocxExporter();
            export.setExporterInput(new SimpleExporterInput(finalReport));
            export.setExporterOutput(new SimpleOutputStreamExporterOutput(reportResultFile));
            export.exportReport();
        }
    }

    @Getter
    public enum ReportOutputFormat {
        HTML("html", "text/html"), XLSX("xlsx", "application/octet-stream"), PDF("pdf", "application/pdf"), DOCX("docx", "application/octet-stream");

        private final String extension;

        private final String mimeType;

        ReportOutputFormat(String extension, String mimeType) {
            this.extension = extension;
            this.mimeType = mimeType;
        }

    }
}
