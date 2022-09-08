package com.tech.truthapp.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PreferencesDTO {

	@ApiModelProperty(hidden = true)
	String prefId;
	String prefType;
	String prefValue;
	@ApiModelProperty(hidden = true)
	Boolean isActive;
	
}
