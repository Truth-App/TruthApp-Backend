package com.tech.truthapp.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BaseDTO {

	protected Date createdAt;
	protected String createdBy;	
	protected Date updatedAt;
	protected String lastModifiedBy;
	protected Integer version;
	
}
