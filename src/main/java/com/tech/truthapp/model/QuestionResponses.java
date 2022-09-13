package com.tech.truthapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class QuestionResponses extends BaseModel{

	String answer;	
	String reviewerId;
	Boolean isPublic;
	Boolean isApproved;
}
