package com.tech.truthapp.dto.prayer;

import com.tech.truthapp.dto.BaseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ValidatePrayerDTO extends BaseDTO{

	String id;
	String prayer;
	String category;
	String subCategory;
	String group;
	Boolean isPublic;
	Boolean isApproved;
	Integer score;
	String tagName;
	String tagId;
}