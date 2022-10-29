package com.tech.truthapp.dto.category;

import com.tech.truthapp.dto.BaseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class SubCategoryDTO extends BaseDTO{

	private String id;
	private String categoryId;
	private String subCategory;
}
