package com.tech.truthapp.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "questions")
@Setter
@Getter
@NoArgsConstructor
public class Question extends BaseModel{

	@Id
	String id;
	String question;
	String questionType;
	String reviewerId;
	String category;
	Integer score;
	Boolean isPublic;
	Boolean isApproved;	
	List<QuestionResponse> responses = new ArrayList<QuestionResponse>();
}
