package com.example.demo.service;

import com.example.demo.collections.Files;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

    private final GridFsTemplate gridFsTemplate;

    private final GridFsOperations operations;

    public String saveDocument(MultipartFile multipartFile) throws IOException {
        return gridFsTemplate.store(multipartFile.getInputStream(), multipartFile.getOriginalFilename(), multipartFile.getContentType()).toString();
    }

    public Files download(String id) throws IOException {
        GridFSFile file =  gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));

        return Files
                .builder()
                .name(file.getFilename())
                .size(file.getLength())
                .type(operations.getResource(file).getContentType())
                .file(new ByteArrayResource(IOUtils.toByteArray(operations.getResource(file).getContent())))
                .build();
    }
}
