package com.tech.truthapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Builder
public class ErrorObject {

	String errorCode;
	String errorField;
	String errorMsg;
	
}
