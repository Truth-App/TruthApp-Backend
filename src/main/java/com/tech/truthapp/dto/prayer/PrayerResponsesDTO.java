package com.tech.truthapp.dto.prayer;

import java.util.ArrayList;
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
public class PrayerResponsesDTO extends BaseDTO{

	String id;
	String response;
	String responderId;
	String reviewerId;
	Boolean isPublic;
	Integer score;
	Boolean isApproved;
	String comments;
	List<PrayerResponseReviewerDTO> reviews = new ArrayList<PrayerResponseReviewerDTO>();
}
