package com.tech.truthapp.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseModel {

	@CreatedDate
	protected LocalDateTime createdOn;
	@CreatedBy
	protected String createdBy;
	
	@LastModifiedDate
	protected LocalDateTime updateOn;
	@LastModifiedBy
	protected String lastModifiedBy;
	@Version
	protected Integer version;
	
}