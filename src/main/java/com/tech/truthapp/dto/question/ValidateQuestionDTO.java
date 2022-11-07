package com.tech.truthapp.dto.question;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ValidateQuestionDTO {

	String id;
	String question;
	String category;
	String subCategory;
	String group;
	Boolean isPublic;
	Boolean isApproved;
	Integer score;
	String tagName;
	String tagId;
}