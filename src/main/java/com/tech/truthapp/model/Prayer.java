package com.tech.truthapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
}
