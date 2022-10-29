package com.tech.truthapp.dto.prayer;

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
public class PrayerDTO extends BaseDTO{

	String id;
	String prayer;
	String category;
	String subCategory;
	String group;
	Boolean isPublic;
	Boolean isApproved;
	Integer score;
	List<PrayerResponsesDTO> responses;
	List<PrayerReviewerDTO> reviews;
}