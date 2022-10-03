package com.tech.truthapp.model.share;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tech.truthapp.model.BaseModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "share")
@Setter
@Getter
@NoArgsConstructor
public class Share extends BaseModel{

	@Id
	String id;
	String share;
	String shareType;	
	String category;
	Integer score;
	Boolean isPublic;
	Boolean isApproved;
	
	List<ShareResponse> responses = new ArrayList<ShareResponse>();
	List<ShareReviewer> reviews = new ArrayList<ShareReviewer>();
}
