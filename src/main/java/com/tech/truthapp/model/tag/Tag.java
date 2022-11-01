package com.tech.truthapp.model.tag;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

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
public class Tag extends BaseModel{

	@Id
	private String id;
	private String tag;
	private String tagType;
	private String category;
	private String subCategory;
	private List<String> subList;
}
