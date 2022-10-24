package com.tech.truthapp.model.question;

import com.tech.truthapp.model.BaseModel;

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
	
}
