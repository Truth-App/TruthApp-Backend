package com.tech.truthapp.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document
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
	Boolean isPublic;
	Boolean isApproved;	
	List<QuestionResponses> responses;
}