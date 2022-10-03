package com.tech.truthapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class QuestionResponseReviewerDTO extends BaseDTO{

	String id;
	String comments;	
	String reviewerId;
	Integer score;
	
}
