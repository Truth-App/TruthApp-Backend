package com.tech.truthapp.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class QuestionResponsesDTO extends BaseDTO{

	String id;
	String response;
	String responderId;
	String reviewerId;
	Boolean isPublic;
	Integer score;
	Boolean isApproved;
	String comments;
	
	List<QuestionResponseReviewerDTO> reviews = new ArrayList<QuestionResponseReviewerDTO>();
}
