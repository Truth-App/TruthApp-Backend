package com.tech.truthapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuestionResponsesDTO extends BaseDTO{

	String answer;	
	String reviewerId;
	Boolean isPublic;
	Boolean isApproved;
}
