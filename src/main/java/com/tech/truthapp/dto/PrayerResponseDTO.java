package com.tech.truthapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PrayerResponseDTO extends BaseDTO{

	Integer id;
	Boolean reviewFalg;
	String userId;
	String reviewedBy;
	String answerUserId;
	String remarks;
	
	
	
	@Override
	public String toString() {
		return "PrayerResponseDTO [id=" + id + ", reviewFalg=" + reviewFalg + ", userId=" + userId + ", reviewedBy="
				+ reviewedBy + ", answerUserId=" + answerUserId + ", remarks=" + remarks + "]";
	}
}
