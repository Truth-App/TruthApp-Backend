package com.tech.truthapp.dto.question;

import java.util.List;

import com.tech.truthapp.dto.BaseDTO;

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
	String category;
	String subCategory;
	String group;
	Boolean isPublic;
	Boolean isApproved;
	Integer score;
	String ageGroup;
	String gender;
	List<QuestionResponsesDTO> responses;
	List<QuestionReviewerDTO> reviews;
	List<String> tags;
}