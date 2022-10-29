package com.tech.truthapp.group.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.tech.truthapp.dto.group.GroupDTO;
import com.tech.truthapp.mapper.EntityMapper;
import com.tech.truthapp.model.group.Group;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GroupMapper extends EntityMapper<GroupDTO, Group> {

	Group toEntity(GroupDTO groupDTO);

	GroupDTO toDto(Group group);

	List<GroupDTO> toDto(List<Group> list);

}
