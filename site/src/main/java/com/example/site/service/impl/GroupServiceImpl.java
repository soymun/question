package com.example.site.service.impl;

import com.example.site.dto.group.GroupCreateDto;
import com.example.site.dto.group.GroupDto;
import com.example.site.exception.NotFoundException;
import com.example.site.mappers.GroupMapper;
import com.example.site.model.Groups;
import com.example.site.repository.GroupRepository;
import com.example.site.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    private final GroupMapper groupMapper;

    @Override
    public List<GroupDto> getAll() {
        return groupRepository.findAll().stream().map(groupMapper::groupToGroupDto).toList();
    }

    @Override
    public GroupDto getById(Long id) {
        return groupMapper.groupToGroupDto(groupRepository.findById(id).orElseThrow(() -> new NotFoundException("Группа не найдена")));
    }

    @Override
    public List<GroupDto> getByName(String name) {
        return groupRepository.getAllByNames(name).stream().map(groupMapper::groupToGroupDto).toList();
    }

    @Override
    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }

    @Override
    public GroupDto updateGroup(GroupDto groupDto) {
        if(groupDto != null){
            Groups findGroups = groupRepository.findById(groupDto.getId()).orElseThrow(() -> new NotFoundException("Группа не найдена"));
            ofNullable(groupDto.getFullName()).ifPresent(findGroups::setFullName);
            ofNullable(groupDto.getShortName()).ifPresent(findGroups::setShortName);
            return groupMapper.groupToGroupDto(groupRepository.save(findGroups));
        }
        throw new IllegalArgumentException("Группа не найдена");
    }

    @Override
    public GroupDto saveGroup(GroupCreateDto groupCreateDto) {
        if(groupCreateDto != null){
            Groups groups = groupMapper.groupCreateDtoToGroup(groupCreateDto);
            return groupMapper.groupToGroupDto(groupRepository.save(groups));
        }
        throw new IllegalArgumentException("Группа не валидна");
    }

    @Override
    public List<GroupDto> getGroupByCourseId(Long id) {
        return groupRepository.getAllGroupsByCourseId(id).stream().map(groupMapper::groupToGroupDto).toList();
    }
}
