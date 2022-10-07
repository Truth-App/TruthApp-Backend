package com.tech.truthapp.dto.share;

import java.util.ArrayList;
import java.util.List;

import com.tech.truthapp.dto.BaseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ShareResponsesDTO extends BaseDTO{

	String id;
	String response;
	String responderId;
	Boolean isPublic;
	Integer score;
	Boolean isApproved;
	String comments;
	String reviewerId;
	
	List<ShareResponseReviewerDTO> reviews = new ArrayList<ShareResponseReviewerDTO>();
}
