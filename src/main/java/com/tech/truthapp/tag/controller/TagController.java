package com.tech.truthapp.tag.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tech.truthapp.dto.tag.TagDTO;
import com.tech.truthapp.tag.service.TagService;

import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api")
@Log4j2
@Api(tags = "Tagging Controller", value = "Tagging Controller", description = "Tagging Controller")
public class TagController {

	/** The Constant ENTITY_NAME. */
	@SuppressWarnings("unused")
	private static final String ENTITY_NAME = "Tagging";

	@Value("${truth-app.clientApp.name}")
	private String applicationName;

	@Autowired
	private TagService tagService;
	
	/**
	 * 
	 * @param tagType
	 * @param keyword
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/tagging/search")
	public ResponseEntity<List<TagDTO>> search(
			@RequestParam(name = "tagType", required = false) String tagType,
			@RequestParam(name = "keyword", required = false) String keyword) throws Exception {
		log.info("REST request to get Search Tags {}, {} ",tagType, keyword);
		List<TagDTO> list = tagService.search(tagType, keyword);
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping("/tagging/filterData")
	public ResponseEntity<List<TagDTO>> filterData(
			@RequestParam(name = "tagType", required = false) String tagType,
			@RequestParam(name = "category", required = false) String category,
			@RequestParam(name = "subCategory", required = false) String subCategory,
			@RequestParam(name = "group", required = false) String group
			) throws Exception {
		log.info("REST request to get filterData {}, {}, {}, {} ", tagType, category, subCategory, group);
		Assert.notNull(tagType, "tagType mustn't be null");
		List<TagDTO> list = tagService.filterData(tagType, category, subCategory, group);
		return ResponseEntity.ok().body(list);
	}
}
