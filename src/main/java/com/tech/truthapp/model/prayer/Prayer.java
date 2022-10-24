package com.tech.truthapp.model.prayer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.tech.truthapp.model.BaseModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(indexName = "prayers")
@Setter
@Getter
@NoArgsConstructor
public class Prayer extends BaseModel{

	@Id
	String id;
	String prayer;
	String prayerType;	
	String category;
	Long score;
	Boolean isPublic;
	Boolean isApproved;
	
	@Field(type = FieldType.Nested)
	List<PrayerResponse> responses = new ArrayList<PrayerResponse>();
	@Field(type = FieldType.Nested)
	List<PrayerReviewer> reviews = new ArrayList<PrayerReviewer>();
}
