package com.tech.truthapp.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BaseDTO {

	protected Date createdOn;
	protected String createdBy;	
	protected Date updateOn;
	protected String lastModifiedBy;
	protected Integer version;
	
}
