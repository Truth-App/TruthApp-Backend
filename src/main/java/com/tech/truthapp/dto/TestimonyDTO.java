package com.tech.truthapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TestimonyDTO extends BaseDTO{

	Integer id;
	String category;
	Boolean reviewFalg;
	String remarks;
	String reviewedBy;
	
	
	@Override
	public String toString() {
		return "TestimonyDTO [id=" + id + ", category=" + category + ", reviewFalg=" + reviewFalg + ", remarks="
				+ remarks + ", reviewedBy=" + reviewedBy + "]";
	}
}
