package com.tech.truthapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(indexName = "testimony")
@Getter
@Setter
@NoArgsConstructor
public class Testimony extends BaseModel {

	@Id
	String id;
	String category;
	Boolean reviewFalg;
	String remarks;
	String reviewedBy;

	@Override
	public int hashCode() {
		return 42;
	}

	@Override
	public String toString() {
		return "Testimony [id=" + id + ", category=" + category + ", reviewFalg=" + reviewFalg + ", remarks=" + remarks
				+ ", reviewedBy=" + reviewedBy + "]";
	}
}
