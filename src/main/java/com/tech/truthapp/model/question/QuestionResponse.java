package com.tech.truthapp.model.question;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.tech.truthapp.model.BaseModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class QuestionResponse  extends BaseModel{

	@Id
	String id;
	String response;	
	String responderId;
	Boolean isPublic;
	Long score;
	Boolean isApproved;
	List<QuestionResponseReviewer> reviews = new ArrayList<QuestionResponseReviewer>();
}
