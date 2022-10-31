package com.tech.truthapp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseDTO<S,E> {

	private S data;
	private E error;
	
}
