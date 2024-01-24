package com.example.site.service;

import com.example.site.dto.history.HistoryDto;

import java.util.List;

public interface TaskHistoryResultService {

    List<HistoryDto> getAllByTask(Long taskId);

    List<HistoryDto> getAllByTaskAndUserId(Long task, Long userId);

    List<HistoryDto> getAllByUserId(Long id);

}
