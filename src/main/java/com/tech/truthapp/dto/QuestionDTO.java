package com.tech.truthapp.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class QuestionDTO extends BaseDTO{

	String id;
	String question;
	String questionType;
	String category;	
	Boolean isPublic;
	Boolean isApproved;
	Integer score;
	List<QuestionResponsesDTO> responses;
	List<QuestionReviewerDTO> reviews;
}