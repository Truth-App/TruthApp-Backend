package com.tech.truthapp.dto.category;

import java.util.List;

import com.tech.truthapp.model.BaseModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class CategoryDTO extends BaseModel {

	private String id;
	private String category;
	private List<SubCategoryDTO> subCategoryList;
	
}
