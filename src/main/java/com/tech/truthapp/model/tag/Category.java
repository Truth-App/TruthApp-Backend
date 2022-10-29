package com.tech.truthapp.model.tag;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.tech.truthapp.model.BaseModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(indexName = "category")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Category extends BaseModel {

	@Id
	private String id;
	private String category;
	@Field(type = FieldType.Nested)
	private List<SubCategory> subCategoryList = new ArrayList<SubCategory>();
}
