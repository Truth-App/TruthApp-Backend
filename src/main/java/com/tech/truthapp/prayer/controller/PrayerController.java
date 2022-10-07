package com.tech.truthapp.prayer.controller;

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

import com.tech.truthapp.dto.prayer.PrayerDTO;
import com.tech.truthapp.dto.prayer.PrayerResponsesDTO;
import com.tech.truthapp.exception.HeaderUtil;
import com.tech.truthapp.prayer.service.PrayerService;
import com.tech.truthapp.prayer.validation.PrayerValidator;

import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api")
@Log4j2
@Api(tags = "Prayer Controller", value = "Prayer Controller", description = "Prayer Controller")
public class PrayerController {

	/** The Constant ENTITY_NAME. */
	private static final String ENTITY_NAME = "Prayer";

	@Value("${truth-app.clientApp.name}")
	private String applicationName;

	@Autowired
	private PrayerValidator prayerValidator;

	@Autowired
	private PrayerService prayerService;

	/**
	 * 
	 * @param prayerDTO
	 * @return prayerDTO
	 * @throws Exception
	 */
	@PostMapping("/prayers")
	public ResponseEntity<PrayerDTO> savePrayer(@Valid @RequestBody PrayerDTO prayerDTO) throws Exception {

		log.debug("REST request to Save Prayer {} ", prayerDTO);
		prayerValidator.validateCreatePrayer(prayerDTO);
		prayerDTO = prayerService.savePrayer(prayerDTO);
		return ResponseEntity
				.created(new URI("/api/prayers/" + prayerDTO.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, prayerDTO.getId().toString()))
				.body(prayerDTO);
	}

	/**
	 * 
	 * @param userId
	 * @return PrayerDTO
	 * @throws Exception
	 */
	@GetMapping("/prayers/{userId}/myprayers")
	public ResponseEntity<List<PrayerDTO>> getPrayersByUserId(@PathVariable("userId") String userId)
			throws Exception {

		log.debug("REST request to get Prayers By User Id {}", userId);
		List<PrayerDTO> list = prayerService.getAllPrayersByUser(userId);
		return ResponseEntity.ok().body(list);
	}
	
	/**
	 * 
	 * @return PrayerDTO
	 * @throws Exception
	 */
	@GetMapping("/prayers/reviewprayers")
	public ResponseEntity<List<PrayerDTO>> getReviewPrayers() throws Exception {
		log.debug("REST request to get Review Prayers");
		List<PrayerDTO> list = prayerService.getReviewPrayers();
		return ResponseEntity.ok().body(list);
	}

	/**
	 * 
	 * @return PrayerDTO
	 * @throws Exception
	 */
	@GetMapping("/prayers/reviewedprayers")
	public ResponseEntity<List<PrayerDTO>> getReviewedPrayers() throws Exception {

		log.debug("REST request to get Reviewed Prayers");
		List<PrayerDTO> list = prayerService.getReviewedPrayers();
		return ResponseEntity.ok().body(list);
	}
	
	/**
	 * 
	 * @return PrayerDTO
	 * @throws Exception
	 */
	@PutMapping("/prayers/{userId}/validateprayer")
	public ResponseEntity<PrayerDTO> reviewPrayers(@PathVariable("userId") String userId,
			@Valid @RequestBody PrayerDTO prayerDTO) throws Exception {
		log.debug("REST request to get Validate Prayer {},{}", userId, prayerDTO);
		PrayerDTO updateObject = prayerService.reviewPrayer(userId, prayerDTO);
		return ResponseEntity.created(new URI("/api/prayers/validateprayer"))
				.headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userId))
				.body(updateObject);
	}
	
	/**
	 * 
	 * @param prayerId
	 * @return PrayerDTO
	 * @throws Exception
	 */
	@PostMapping("/prayers/{prayerId}/responses")
	public ResponseEntity<PrayerDTO> createResponse(@PathVariable("prayerId") String prayerId,
			@Valid @RequestBody PrayerResponsesDTO responseDTO) throws Exception {
		log.debug("REST request to get create Response for Prayer {},{}", prayerId, responseDTO);
		PrayerDTO updateObject = prayerService.createResponse(prayerId, responseDTO);
		return ResponseEntity.created(new URI("/api/prayers/responses"))
				.headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, prayerId))
				.body(updateObject);
	}
	
	/**
	 * 
	 * @return prayerResponsesDTO
	 * @throws Exception
	 */
	@GetMapping("/prayers/{prayerId}/responses")
	public ResponseEntity<List<PrayerResponsesDTO>> getResponse(
			@PathVariable("prayerId") String prayerId) throws Exception {
		log.debug("REST request to get Get Response for Prayer {}", prayerId);
		List<PrayerResponsesDTO> responses = prayerService.getPrayerResponse(prayerId);
		return ResponseEntity.ok().body(responses);
	}

	/**
	 * 
	 * @return prayerDTO
	 * @throws Exception
	 */
	@PutMapping("/prayers/{prayerId}/responses/{userId}/validateresponses")
	public ResponseEntity<PrayerDTO> validateResponse(@PathVariable("prayerId") String prayerId,
			@PathVariable("userId") String userId,
			@Valid @RequestBody PrayerResponsesDTO responseDTO) throws Exception {
		responseDTO.setReviewerId(userId);
		log.debug("REST request to get Validate Response for Prayer {},{}", prayerId, responseDTO);
		PrayerDTO updateObject = prayerService.validatePrayerResponse(prayerId, responseDTO);
		return ResponseEntity.created(new URI("/api/prayers/validateresponses"))
				.headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, prayerId))
				.body(updateObject);
	}
	
	/**
	 * 
	 * 
	 * @return List
	 * @throws Exception
	 */
	@GetMapping("/prayers")
	public ResponseEntity<List<PrayerDTO>> getAlPrayers() throws Exception {

		log.debug("REST request to Get All Prayers ");
		List<PrayerDTO> list = prayerService.getAllPrayers();
		return ResponseEntity.ok().body(list);
	}
	
	/**
	 * 
	 * @return prayerDTO
	 * @throws Exception
	 */
	@GetMapping("/prayers/reviewresponses")
	public ResponseEntity<List<PrayerDTO>> getReviewResponses() throws Exception {
		log.debug("REST request to get Review Prayers");
		List<PrayerDTO> userQuestionList = prayerService.getReviewResponses();
		return ResponseEntity.ok().body(userQuestionList);
	}
	
	/**
	 * 
	 * @return prayerDTO
	 * @throws Exception
	 */
	@GetMapping("/prayers/categoryreviewedprayers")
	public ResponseEntity<List<PrayerDTO>> getReviewedQuestionsForCategory
				(@RequestParam(name = "category") String category) throws Exception {

		log.debug("REST request to get Reviewed Prayers for category {} ", category);
		List<PrayerDTO> list = prayerService.getReviewedPrayersForCategory(category);
		return ResponseEntity.ok().body(list);
	}
	
	/**
	 * 
	 * @return prayerDTO
	 * @throws Exception
	 */
	@GetMapping("/prayers/{userId}/myreviewedresponses")
	public ResponseEntity<List<PrayerDTO>> getMyReviewedResponses(@PathVariable("userId") String userId) throws Exception {
		log.debug("REST request to get Review Prayers");
		List<PrayerDTO> list = prayerService.getMyReviewedResponses(userId);
		return ResponseEntity.ok().body(list);
	}
	
}
