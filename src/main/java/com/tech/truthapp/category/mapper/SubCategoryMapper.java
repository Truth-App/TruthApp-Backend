package com.tech.truthapp.category.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.tech.truthapp.dto.category.SubCategoryDTO;
import com.tech.truthapp.mapper.EntityMapper;
import com.tech.truthapp.model.tag.SubCategory;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubCategoryMapper extends EntityMapper<SubCategoryDTO, SubCategory> {

	SubCategory toEntity(SubCategoryDTO subCategoryDTO);

	SubCategoryDTO toDto(SubCategory subCategory);

	List<SubCategoryDTO> toDto(List<SubCategory> list);

}
