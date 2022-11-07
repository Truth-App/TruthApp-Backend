package com.tech.truthapp.dto.tag;

import java.util.ArrayList;
import java.util.List;

import com.tech.truthapp.dto.BaseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class TagDTO extends BaseDTO {

	private String id;
	private String tag;
	private String tagType;
	private String category;
	private String subCategory;
	private String group;
	private List<String> subList = new ArrayList<String>();
}
