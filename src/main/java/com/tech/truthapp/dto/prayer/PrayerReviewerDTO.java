package com.tech.truthapp.dto.prayer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class PrayerReviewerDTO {

	String id;
	String comments;	
	String reviewerId;
	
}
