package com.tech.truthapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PrayerRequestDTO extends BaseDTO{

	Integer id;
	String category;
	Boolean reviewFalg;
	String remarks;
	String userId;
	String reviewedBy;
	
	
	
	@Override
	public String toString() {
		return "PrayerRequestDTO [id=" + id + ", category=" + category + ", reviewFalg=" + reviewFalg + ", remarks="
				+ remarks + ", userId=" + userId + ", reviewedBy=" + reviewedBy + "]";
	}
	
	
}
