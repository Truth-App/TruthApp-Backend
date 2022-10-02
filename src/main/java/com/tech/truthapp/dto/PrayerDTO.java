package com.tech.truthapp.dto;

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
	String prayerType;
	String category;	
	Boolean isPublic;
	Boolean isApproved;
	Integer score;
	
}