package com.example.site.service;

import com.example.site.dto.group.GroupCreateDto;
import com.example.site.dto.group.GroupDto;

import java.util.List;

public interface GroupService {

    List<GroupDto> getAll();

    GroupDto getById(Long id);

    List<GroupDto> getByName(String name);

    void deleteGroup(Long id);

    GroupDto updateGroup(GroupDto groupDto);

    GroupDto saveGroup(GroupCreateDto groupCreateDto);

    List<GroupDto> getGroupByCourseId(Long id);
}
