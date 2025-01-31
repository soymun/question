package com.example.site.service.impl;

import com.example.site.dto.RequestExecuteSql;
import com.example.site.dto.ResponseExecuteSql;
import com.example.site.dto.sandbox.SandBoxOpenDto;
import com.example.site.dto.sandbox.SandboxDto;
import com.example.site.dto.sandbox.SandboxExecuteDto;
import com.example.site.exception.ForbiddenException;
import com.example.site.exception.NotFoundException;
import com.example.site.mappers.SandBoxMapper;
import com.example.site.model.Sandbox;
import com.example.site.repository.SandboxRepository;
import com.example.site.security.UserDetailImpl;
import com.example.site.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SandboxService {

    private final RabbitTemplate rabbitTemplate;

    private final SandboxRepository sandboxRepository;

    private final SandBoxMapper sandBoxMapper;


    @Transactional(readOnly = true)
    public List<ResponseExecuteSql> processInSandbox(SandboxExecuteDto sandboxExecuteDto) {

        UserDetailImpl userDetail = SecurityUtil.getUserDetail();

        List<Sandbox> findAllSandbox = sandboxRepository.findAll();

        Sandbox currentSandBox;

        if (!findAllSandbox.isEmpty()) {
            currentSandBox = findAllSandbox.get(0);
        } else {
            throw new NotFoundException("Sandbox not found");
        }

        if (currentSandBox.getSchemaName() != null && currentSandBox.getOpen()) {
            return rabbitTemplate.convertSendAndReceiveAsType("postgresql-execute", RequestExecuteSql
                    .builder()
                    .admin(true)
                    .sandBox(true)
                    .userId(userDetail.getId())
                    .schema(currentSandBox.getSchemaName())
                    .userSql(sandboxExecuteDto.getSql())
                    .build(), new ParameterizedTypeReference<List<ResponseExecuteSql>>() {
                @Override
                public Type getType() {
                    return List.class;
                }
            });
        } else {
            throw new ForbiddenException("Sandbox is not open in current database");
        }
    }

    @Transactional
    public void closeSandBox() {
        sandboxRepository.closeSandbox();
    }

    @Transactional
    public void openSandbox() {
        sandboxRepository.openSandbox();
    }

    public void saveSandbox(SandboxDto sandboxDto) {
        sandboxRepository.save(sandBoxMapper.dtoToSandBox(sandboxDto));
    }

    public SandboxDto getSandbox() {
        List<Sandbox> findAllSandbox = sandboxRepository.findAll();

        Sandbox currentSandBox;

        if (!findAllSandbox.isEmpty()) {
            currentSandBox = findAllSandbox.get(0);
        } else {
            throw new NotFoundException("Sandbox not found");
        }

        return sandBoxMapper.sandboxDtoToSandbox(currentSandBox);
    }

    public SandBoxOpenDto getOpen() {

        List<Sandbox> findAllSandbox = sandboxRepository.findAll();

        Sandbox currentSandBox;

        if (!findAllSandbox.isEmpty()) {
            currentSandBox = findAllSandbox.get(0);
        } else {
            throw new NotFoundException("Sandbox not found");
        }

        return SandBoxOpenDto.builder().open(currentSandBox.getOpen()).build();

    }


    public void executeClearSandBox() {
        List<Sandbox> findAllSandbox = sandboxRepository.findAll();

        Sandbox currentSandBox;

        if (!findAllSandbox.isEmpty()) {
            currentSandBox = findAllSandbox.get(0);
        } else {
            throw new NotFoundException("Sandbox not found");
        }

        if (currentSandBox.getSchemaName() != null) {
            rabbitTemplate.convertSendAndReceiveAsType("postgresql-execute", RequestExecuteSql
                    .builder()
                    .admin(true)
                    .sandBox(true)
                    .userId(null)
                    .schema(currentSandBox.getSchemaName())
                    .userSql(currentSandBox.getSqlClear())
                    .build(), new ParameterizedTypeReference<List<ResponseExecuteSql>>() {
                @Override
                public Type getType() {
                    return List.class;
                }
            });
        } else {
            throw new ForbiddenException("Sandbox is not open in current database");
        }
    }
}
