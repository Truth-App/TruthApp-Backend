package com.tech.truthapp.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuestionDTO extends BaseDTO{

	String id;
	String question;
	String questionType;
	String category;
	String reviewerId;
	Boolean isPublic;
	Boolean isApproved;	
	List<QuestionResponsesDTO> responses;
}
