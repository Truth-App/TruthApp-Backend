package com.tech.truthapp.share.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tech.truthapp.dto.share.ShareDTO;
import com.tech.truthapp.dto.share.ShareResponsesDTO;
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
		ShareDTO updateObject = shareService.reviewShare(userId, shareDTO);
		return ResponseEntity.created(new URI("/api/share/validateshare"))
				.headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userId))
				.body(updateObject);
	}
	
	/**
	 * 
	 * @param shareId
	 * @return ShareDTO
	 * @throws Exception
	 */
	@PostMapping("/share/{shareId}/responses")
	public ResponseEntity<ShareDTO> createResponse(@PathVariable("shareId") String shareId,
			@Valid @RequestBody ShareResponsesDTO responseDTO) throws Exception {
		log.debug("REST request to get create Response for Share {},{}", shareId, responseDTO);
		ShareDTO updateObject = shareService.createResponse(shareId, responseDTO);
		return ResponseEntity.created(new URI("/api/share/responses"))
				.headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shareId))
				.body(updateObject);
	}
	
	/**
	 * 
	 * @return shareResponsesDTO
	 * @throws Exception
	 */
	@GetMapping("/share/{shareId}/responses")
	public ResponseEntity<List<ShareResponsesDTO>> getResponse(
			@PathVariable("shareId") String shareId) throws Exception {
		log.debug("REST request to get Get Response for Share {}", shareId);
		List<ShareResponsesDTO> responses = shareService.getShareResponse(shareId);
		return ResponseEntity.ok().body(responses);
	}

	/**
	 * 
	 * @return ShareDTO
	 * @throws Exception
	 */
	@PutMapping("/share/{shareId}/responses/{userId}/validateresponses")
	public ResponseEntity<ShareDTO> validateResponse(@PathVariable("shareId") String shareId,
			@PathVariable("userId") String userId,
			@Valid @RequestBody ShareResponsesDTO responseDTO) throws Exception {
		responseDTO.setReviewerId(userId);
		log.debug("REST request to get Validate Response for Share {},{}", shareId, responseDTO);
		ShareDTO updateObject = shareService.validateResponse(shareId, responseDTO);
		return ResponseEntity.created(new URI("/api/share/validateresponses"))
				.headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shareId))
				.body(updateObject);
	}
	
	/**
	 * 
	 * 
	 * @return List
	 * @throws Exception
	 */
	@GetMapping("/share")
	public ResponseEntity<List<ShareDTO>> getAlShares() throws Exception {
		log.debug("REST request to Get All Shares ");
		List<ShareDTO> list = shareService.getAllSahres();
		return ResponseEntity.ok().body(list);
	}
	
	/**
	 * 
	 * @return ShareDTO
	 * @throws Exception
	 */
	@GetMapping("/share/reviewresponses")
	public ResponseEntity<List<ShareDTO>> getReviewResponses() throws Exception {
		log.debug("REST request to get Review Share");
		List<ShareDTO> list = shareService.getReviewResponses();
		return ResponseEntity.ok().body(list);
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
		List<ShareDTO> list = shareService.getReviewedShareForCategory(category);
		return ResponseEntity.ok().body(list);
	}
	
	/**
	 * 
	 * @return prayerDTO
	 * @throws Exception
	 */
	@GetMapping("/share/{userId}/myreviewedresponses")
	public ResponseEntity<List<ShareDTO>> getMyReviewedResponses(@PathVariable("userId") String userId) throws Exception {
		log.debug("REST request to get Review Prayers");
		List<ShareDTO> list = shareService.getMyReviewedResponses(userId);
		return ResponseEntity.ok().body(list);
	}
	
}
