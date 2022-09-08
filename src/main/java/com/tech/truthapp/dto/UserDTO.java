package com.tech.truthapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

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
}
