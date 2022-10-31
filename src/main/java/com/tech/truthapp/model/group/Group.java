package com.tech.truthapp.model.group;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import com.tech.truthapp.model.BaseModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(indexName = "group")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Group extends BaseModel{

	@Id
	private String id;
	private String group;
}
