package com.tech.truthapp.model.prayer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tech.truthapp.model.BaseModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "prayers")
@Setter
@Getter
@NoArgsConstructor
public class Prayer extends BaseModel{

	@Id
	String id;
	String prayer;
	String prayerType;	
	String category;
	Integer score;
	Boolean isPublic;
	Boolean isApproved;
	
	List<PrayerResponse> responses = new ArrayList<PrayerResponse>();
	List<PrayerReviewer> reviews = new ArrayList<PrayerReviewer>();
}
