package com.tech.truthapp.dto.share;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class ShareReviewerDTO {

	String id;
	String comments;	
	String reviewerId;
	
}
