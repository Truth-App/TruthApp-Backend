package com.tech.truthapp.model.tag;

import org.springframework.data.annotation.Id;

import com.tech.truthapp.model.BaseModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class SubCategory extends BaseModel{

	@Id
	private String id;
	private String categoryId;
	private String subCategory;
}
