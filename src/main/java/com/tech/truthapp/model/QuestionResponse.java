package com.tech.truthapp.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class QuestionResponse extends BaseModel{

	String id;
	String response;	
	String reviewerId;
	Boolean isPublic;
	Integer score;
	Boolean isApproved;
	List<QuestionResponseReviewer> reviews = new ArrayList<QuestionResponseReviewer>();
}
