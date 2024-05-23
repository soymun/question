package com.example.site.service.impl;

import com.example.site.service.FileService;
import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final MinioClient minioClient;

    @Override
    public String uploadFile(MultipartFile multipartFile, String bucket) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        log.info("File upload {}", multipartFile.getName());

        String[] type = multipartFile.getName().split("\\.");

        String random = UUID.randomUUID() + type[type.length-1];

        minioClient.putObject(PutObjectArgs
                .builder()
                        .object(random)
                        .stream(new ByteArrayInputStream(multipartFile.getBytes()), multipartFile.getSize(), -1)
                        .contentType(multipartFile.getContentType())
                        .bucket(bucket)
                .build());

        return random;
    }

    @Override
    public Resource downloadFile(String fileName, String bucket) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        log.info("Download file {}", fileName);

        return new ByteArrayResource(minioClient.getObject(GetObjectArgs.builder().object(fileName).bucket(bucket).build()).readAllBytes());
    }

    @Override
    public void deleteFile(String fileName, String bucket) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        log.info("Delete file {}", fileName);

        minioClient.removeObject(RemoveObjectArgs.builder().object(fileName).bucket(bucket).build());
    }
}
