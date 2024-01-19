package com.example.site.mappers;

import com.example.site.dto.group.GroupCreateDto;
import com.example.site.dto.group.GroupDto;
import com.example.site.model.Groups;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    Groups groupCreateDtoToGroup(GroupCreateDto groupCreateDto);

    GroupDto groupToGroupDto(Groups groups);
}
