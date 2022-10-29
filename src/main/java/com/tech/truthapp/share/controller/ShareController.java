package com.tech.truthapp.share.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tech.truthapp.dto.share.ShareDTO;
import com.tech.truthapp.exception.HeaderUtil;
import com.tech.truthapp.share.service.ShareService;
import com.tech.truthapp.share.validation.ShareValidator;

import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api")
@Log4j2
@Api(tags = "Share Controller", value = "Share Controller", description = "Share Controller")
public class ShareController {

	/** The Constant ENTITY_NAME. */
	private static final String ENTITY_NAME = "Share";

	@Value("${truth-app.clientApp.name}")
	private String applicationName;

	@Autowired
	private ShareValidator shareValidator;

	@Autowired
	private ShareService shareService;
	
	
	/**
	 * 
	 * @param shareDTO
	 * @return shareDTO
	 * @throws Exception
	 */
	@PostMapping("/share")
	public ResponseEntity<ShareDTO> saveShare(@Valid @RequestBody ShareDTO shareDTO) throws Exception {

		log.debug("REST request to Save Share {} ", shareDTO);
		shareValidator.validateCreate(shareDTO);
		shareDTO = shareService.saveShare(shareDTO);
		return ResponseEntity
				.created(new URI("/api/share/" + shareDTO.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shareDTO.getId().toString()))
				.body(shareDTO);
	}
	
	/**
	 * 
	 * @return ShareDTO
	 * @throws Exception
	 */
	@GetMapping("/share/reviewshare")
	public ResponseEntity<List<ShareDTO>> getReviewShares() throws Exception {
		log.debug("REST request to get Review Shares");
		List<ShareDTO> list = shareService.getReviewShares();
		return ResponseEntity.ok().body(list);
	}

	/**
	 * 
	 * @return ShareDTO
	 * @throws Exception
	 */
	@GetMapping("/share/reviewedshare")
	public ResponseEntity<List<ShareDTO>> getReviewedShares() throws Exception {

		log.debug("REST request to get Reviewed Shares");
		List<ShareDTO> list = shareService.getReviewedShares();
		return ResponseEntity.ok().body(list);
	}
	
	/**
	 * 
	 * @return ShareDTO
	 * @throws Exception
	 */
	@PutMapping("/share/{userId}/validateshare")
	public ResponseEntity<ShareDTO> validateshare(@PathVariable("userId") String userId,
			@Valid @RequestBody ShareDTO shareDTO) throws Exception {
		log.debug("REST request to get Validate Share {},{}", userId, shareDTO);
		ShareDTO updateObject = shareService.validateShare(userId, shareDTO);
		return ResponseEntity.created(new URI("/api/share/validateshare"))
				.headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userId))
				.body(updateObject);
	}
	
	/**
	 * 
	 * @param userId
	 * @return ShareDTO
	 * @throws Exception
	 */
	@GetMapping("/share/{userId}/myshare")
	public ResponseEntity<List<ShareDTO>> getSharesByUserId(@PathVariable("userId") String userId)
			throws Exception {

		log.debug("REST request to get Shares By User Id {}", userId);
		List<ShareDTO> userList = shareService.getAllSharesByUser(userId);
		return ResponseEntity.ok().body(userList);
	}
	
	/**
	 * 
	 * @return prayerDTO
	 * @throws Exception
	 */
	@GetMapping("/share/categoryreviewedshare")
	public ResponseEntity<List<ShareDTO>> getReviewedQuestionsForCategory
				(@RequestParam(name = "category") String category) throws Exception {

		log.debug("REST request to get Reviewed Share for category {} ", category);
		List<ShareDTO> list = shareService.getReviewedSharesByCategory(category);
		return ResponseEntity.ok().body(list);
	}
	
	/**
	 * 
	 * @param category
	 * @param subCategory
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/share/categorysubcategoryreviewedshare")
	public ResponseEntity<List<ShareDTO>> getReviewedQuestionsByCategoryAndSubCategory
				(@RequestParam(name = "category") String category, 
				@RequestParam(name = "subcategory") String subCategory) throws Exception {

		log.debug("REST request to get Reviewed shares for category {} ", category);
		List<ShareDTO> list = shareService.getReviewedSharesByCategoryAndSubCategory(category, subCategory);
		return ResponseEntity.ok().body(list);
	}
	
	/**
	 * 
	 * @param group
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/share/groupeviewedquestions")
	public ResponseEntity<List<ShareDTO>> getReviewedQuestionsByGroup
				(@RequestParam(name = "group") String group) throws Exception {

		log.debug("REST request to get Reviewed shares for group {} ", group);
		List<ShareDTO> list = shareService.getReviewedSharesByGroup(group);
		return ResponseEntity.ok().body(list);
	}
	
	@DeleteMapping("/share/{shareId}")
	public ResponseEntity<ShareDTO> deleteShare(
			@PathVariable("shareId") String shareId) throws Exception {
		log.debug("REST request to get Delete Share {}", shareId);
		ShareDTO shareDTO = shareService.deleteShare(shareId);
		return ResponseEntity.ok().body(shareDTO);
	}
	
	@PutMapping("/share/{shareId}")
	public ResponseEntity<ShareDTO> updateShare(@PathVariable("shareId") String shareId,
			@Valid @RequestBody ShareDTO shareDTO) throws Exception {
		log.debug("REST request to get update share {},{}", shareId, shareDTO);
		shareDTO.setId(shareId);
		ShareDTO updateObject = shareService.updateShare(shareId, shareDTO);
		return ResponseEntity.created(new URI("/api/questions/reviewedquestions"))
				.headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shareId))
				.body(updateObject);
	}
	
	
}
