package com.example.site.mappers;

import com.example.site.dto.sandbox.SandboxDto;
import com.example.site.model.Sandbox;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SandBoxMapper {

    Sandbox dtoToSandBox(SandboxDto sandboxDto);

    SandboxDto sandboxDtoToSandbox(Sandbox sandbox);
}
