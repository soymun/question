package com.example.site.mappers;

public interface DefaultMapper<CREATE, UPDATE, DTO, ENTITY> {

    ENTITY createToEntity(CREATE c);

    DTO entityToDto(ENTITY e);

    ENTITY updateToEntity(UPDATE u);
}
