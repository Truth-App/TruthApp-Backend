package com.tech.truthapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class QuestionResponseReviewer extends BaseModel{

	String id;
	String comments;	
	String reviewerId;
	Integer score;
	
}
