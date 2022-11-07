package com.tech.truthapp.tag.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.tech.truthapp.dto.tag.TagDTO;
import com.tech.truthapp.mapper.EntityMapper;
import com.tech.truthapp.model.tag.Tag;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper extends EntityMapper<TagDTO, Tag>{
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	Tag toEntity(TagDTO dto);
	
	

	/**
	 * 
	 * @param tag
	 * @return
	 */
	TagDTO toDto(Tag tag);

	/**
	 * 
	 * @param tags
	 * @return
	 */
	List<TagDTO> toDto(List<Tag> tags);

}
