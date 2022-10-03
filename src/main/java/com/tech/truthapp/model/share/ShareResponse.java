package com.tech.truthapp.model.share;

import java.util.ArrayList;
import java.util.List;

import com.tech.truthapp.model.BaseModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class ShareResponse extends BaseModel{

	String id;
	String response;	
	String reviewerId;
	Boolean isPublic;
	Integer score;
	Boolean isApproved;
	List<ShareResponseReviewer> reviews = new ArrayList<ShareResponseReviewer>();
}
