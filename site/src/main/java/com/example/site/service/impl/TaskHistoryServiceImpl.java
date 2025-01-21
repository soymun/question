package com.example.site.service.impl;

import com.example.site.dto.history.HistoryDto;
import com.example.site.mappers.TaskHistoryMapper;
import com.example.site.repository.TaskHistoryResultRepository;
import com.example.site.security.UserDetailImpl;
import com.example.site.service.TaskHistoryResultService;
import com.example.site.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskHistoryServiceImpl implements TaskHistoryResultService {

    private final TaskHistoryResultRepository repository;

    private final TaskHistoryMapper taskHistoryMapper;

    @Override
    public List<HistoryDto> getAllByTask(Long taskId) {
        return repository.getAllByTaskId(taskId).stream().map(taskHistoryMapper::taskHistoryToDto).toList();
    }

    @Override
    public List<HistoryDto> getAllByTaskAndUserId(Long task) {

        UserDetailImpl userDetail = SecurityUtil.getUserDetail();

        return repository.getAllByTaskIdAndUserId(task, userDetail.getId()).stream().map(taskHistoryMapper::taskHistoryToDto).toList();
    }

    @Override
    public List<HistoryDto> getAllByUserId() {

        UserDetailImpl userDetail = SecurityUtil.getUserDetail();

        return repository.getAllByUserId(userDetail.getId()).stream().map(taskHistoryMapper::taskHistoryToDto).toList();
    }
}
