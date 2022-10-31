package com.tech.truthapp.dto.share;

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
public class ShareDTO extends BaseDTO{

	String id;
	String share;
	String shareType;
	String category;
	String subCategory;
	String group;	
	Boolean isPublic;
	Boolean isApproved;
	Long score;
	List<ShareResponsesDTO> responses;
	List<ShareReviewerDTO> reviews;
}