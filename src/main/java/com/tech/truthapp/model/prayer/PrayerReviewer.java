package com.tech.truthapp.model.prayer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class PrayerReviewer {

	String id;
	String comments;	
	String reviewerId;
	Integer score;
	
}
