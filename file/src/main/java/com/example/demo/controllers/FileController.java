package com.example.demo.controllers;

import com.example.demo.collections.Files;
import com.example.demo.model.FileResponse;
import com.example.demo.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/files")
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<FileResponse> upload(@RequestPart MultipartFile multipartFile) throws IOException {
        return ResponseEntity.ok(FileResponse.builder()
                .id(fileService.saveDocument(multipartFile)).build());
    }

    @PostMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable String id) throws IOException {
        Files files = fileService.download(id);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + files.getName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, files.getType())
                .body(files.getFile());
    }
}
