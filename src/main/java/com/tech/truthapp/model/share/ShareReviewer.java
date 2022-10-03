package com.tech.truthapp.model.share;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class ShareReviewer {

	String id;
	String comments;	
	String reviewerId;
	Integer score;
	
}
