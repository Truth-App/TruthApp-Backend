package com.tech.truthapp.category.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.tech.truthapp.dto.category.CategoryDTO;
import com.tech.truthapp.mapper.EntityMapper;
import com.tech.truthapp.model.tag.Category;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {

	Category toEntity(CategoryDTO categoryDTO);

	CategoryDTO toDto(Category category);

	List<CategoryDTO> toDto(List<Category> list);

}
