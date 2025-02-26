package com.example.site.controllers;

import com.example.site.dto.ResultDto;
import com.example.site.service.FileService;
import com.example.site.util.BucketUtil;
import io.minio.GetObjectResponse;
import io.minio.errors.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;


    @Operation(description = "Добавить файл")
    @PostMapping("/save/file")
    public ResponseEntity<ResultDto<String>> saveFile(@RequestPart MultipartFile multipartFile) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return ResponseEntity.ok(new ResultDto<>(fileService.uploadFile(multipartFile, BucketUtil.Buckets.FILES.value)));
    }


    @Operation(description = "Добавить отчёт")
    @PostMapping("/save/report")
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    public ResponseEntity<ResultDto<String>> saveReport(@RequestPart MultipartFile multipartFile) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return ResponseEntity.ok(new ResultDto<>(fileService.uploadFile(multipartFile, BucketUtil.Buckets.REPORTS.value)));
    }

    @Operation(description = "Получить файл")
    @GetMapping(value = "/file/{name}", produces = {"application/octet-stream"})
    public ResponseEntity<Resource> getFile(@PathVariable String name) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + name + "\"").body(fileService.downloadFile(name, BucketUtil.Buckets.FILES.value));
    }

    @Operation(description = "Получить файл")
    @GetMapping(value = "/file/jpg/{name}", produces = "image/png")
    public ResponseEntity<Resource> getFileJpg(@PathVariable String name) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return ResponseEntity.ok(fileService.downloadFile(name, BucketUtil.Buckets.FILES.value));
    }


    @Operation(description = "Получить отчёт")
    @GetMapping("/report/{name}")
    public ResponseEntity<Resource> getReport(@PathVariable String name) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return ResponseEntity.ok(fileService.downloadFile(name, BucketUtil.Buckets.REPORTS.value));
    }

    @Operation(description = "Удаление файла")
    @DeleteMapping("/file/{name}")
    public ResponseEntity<ResultDto<String>> deleteFile(@PathVariable String name) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        fileService.deleteFile(name, BucketUtil.Buckets.FILES.value);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Удаление отчёта")
    @DeleteMapping("/report/{name}")
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    public ResponseEntity<ResultDto<String>> deleteReport(@PathVariable String name) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        fileService.deleteFile(name, BucketUtil.Buckets.REPORTS.value);
        return ResponseEntity.ok().build();
    }

}
