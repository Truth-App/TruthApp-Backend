package com.tech.truthapp.model.question;

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

@Document(indexName = "question")
@Setter
@Getter
@NoArgsConstructor
public class Question extends BaseModel{

	@Id
	String id;
	String question;
	String category;
	String subCategory;
	String group;
	Long score;
	Boolean isPublic;
	Boolean isApproved;
	@Field(type = FieldType.Nested)
	List<QuestionResponse> responses = new ArrayList<QuestionResponse>();
	@Field(type = FieldType.Nested)
	List<QuestionReviewer> reviews = new ArrayList<QuestionReviewer>();
}
