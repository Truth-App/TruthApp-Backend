package com.tech.truthapp.dto.share;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ValidateShareDTO {

	String id;
	String share;
	String shareType;
	String category;
	String subCategory;
	String group;	
	Boolean isPublic;
	Boolean isApproved;
	Long score;
	String tagName;
	String tagId;
}