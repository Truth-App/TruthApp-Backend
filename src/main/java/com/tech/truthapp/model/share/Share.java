package com.tech.truthapp.model.share;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.tech.truthapp.model.BaseModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(indexName = "share")
@Setter
@Getter
@NoArgsConstructor
public class Share extends BaseModel {

	@Id
	String id;
	String share;
	String shareType;
	String category;
	Integer score;
	Boolean isPublic;
	Boolean isApproved;

	@Field(type = FieldType.Nested)
	List<ShareResponse> responses = new ArrayList<ShareResponse>();
	@Field(type = FieldType.Nested)
	List<ShareReviewer> reviews = new ArrayList<ShareReviewer>();
}
