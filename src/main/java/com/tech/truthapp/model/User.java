package com.tech.truthapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(indexName = "users")
@Setter
@Getter
@NoArgsConstructor
public class User {

	@Transient
    public static final String SEQUENCE_NAME = "user_sequence";
	
	@Id
	private String id;
	
	String userId;
	String mobile;
	String email;
	String age;
	String gender;	
	String occupation;
	String salvation;
	String experienceWithGod;
	String userType;
	Boolean userActive;
	
	public boolean isNew() { return (getUserId() == null); }
}
