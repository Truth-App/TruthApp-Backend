package com.tech.truthapp.dto;

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
	String reviewerId;
	Boolean isPublic;
	Integer score;
	Boolean isApproved;
}
