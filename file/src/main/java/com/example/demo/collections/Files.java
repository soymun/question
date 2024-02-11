package com.example.demo.collections;

import lombok.Builder;
import lombok.Data;
import org.bson.types.Binary;
import org.springframework.core.io.Resource;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.gridfs.GridFsObject;

import java.io.InputStream;

@Data
@Builder
public class Files {

    @Id
    private String id;

    private String name;

    private String type;

    private Long size;

    private Resource file;
}
