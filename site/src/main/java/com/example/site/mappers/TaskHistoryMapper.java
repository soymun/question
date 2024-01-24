package com.example.site.mappers;

import com.example.site.dto.history.HistoryDto;
import com.example.site.model.TaskHistoryResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskHistoryMapper {

    HistoryDto taskHistoryToDto(TaskHistoryResult taskHistoryResult);
}
