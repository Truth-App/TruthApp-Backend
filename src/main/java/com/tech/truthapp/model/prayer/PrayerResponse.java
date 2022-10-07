package com.tech.truthapp.model.prayer;

import java.util.ArrayList;
import java.util.List;

import com.tech.truthapp.model.BaseModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class PrayerResponse extends BaseModel{

	String id;
	String response;
	String responderId;
	Boolean isPublic;
	Integer score;
	Boolean isApproved;
	List<PrayerResponseReviewer> reviews = new ArrayList<PrayerResponseReviewer>();
}
