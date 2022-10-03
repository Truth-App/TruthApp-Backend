package com.tech.truthapp.question.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.tech.truthapp.dto.QuestionDTO;
import com.tech.truthapp.dto.QuestionResponsesDTO;
import com.tech.truthapp.exception.HeaderUtil;
import com.tech.truthapp.question.service.QuestionService;
import com.tech.truthapp.question.validation.QuestionValidator;

import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api")
@Log4j2
@Api(tags = "Question Controller", value = "Question Controller", description = "Question Controller")
public class QuestionController {

	/** The Constant ENTITY_NAME. */
	private static final String ENTITY_NAME = "Question";

	@Value("${truth-app.clientApp.name}")
	private String applicationName;

	@Autowired
	private QuestionValidator questionValidator;

	@Autowired
	private QuestionService questionService;

	/**
	 * 
	 * @param questionDTO
	 * @return questionDTO
	 * @throws Exception
	 */
	@PostMapping("/questions")
	public ResponseEntity<QuestionDTO> saveQuestion(@Valid @RequestBody QuestionDTO questionDTO) throws Exception {

		log.debug("REST request to Save Question {} ", questionDTO);
		questionValidator.validateCreateQuestion(questionDTO);
		questionDTO = questionService.saveQuestion(questionDTO);
		return ResponseEntity
				.created(new URI("/api/questions/" + questionDTO.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, questionDTO.getId().toString()))
				.body(questionDTO);
	}

	/**
	 * 
	 * @param questionDTO
	 * @return questionDTO
	 * @throws Exception
	 */
	@GetMapping("/questions/{userId}/myquestions")
	public ResponseEntity<List<QuestionDTO>> getQuestionsByUserId(@PathVariable("userId") String userId)
			throws Exception {

		log.debug("REST request to get Questions By User Id {}", userId);
		List<QuestionDTO> userQuestionList = questionService.getAllQuestionsByUser(userId);
		return ResponseEntity.created(new URI("/api/questions/" + userId))
				.headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userId))
				.body(userQuestionList);
	}

	/**
	 * 
	 * @param questionDTO
	 * @return questionDTO
	 * @throws Exception
	 */
	@GetMapping("/questions/reviewedquestions")
	public ResponseEntity<List<QuestionDTO>> getReviewedQuestions() throws Exception {

		log.debug("REST request to get Reviewed Questions");
		List<QuestionDTO> userQuestionList = questionService.getReviewedQuestions();
		return ResponseEntity.created(new URI("/api/questions/reviewedquestions"))
				.headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ENTITY_NAME))
				.body(userQuestionList);
	}

	/**
	 * 
	 * @param questionDTO
	 * @return questionDTO
	 * @throws Exception
	 */
	@GetMapping("/questions/reviewquestions")
	public ResponseEntity<List<QuestionDTO>> getReviewQuestions() throws Exception {
		log.debug("REST request to get Review Questions");
		List<QuestionDTO> userQuestionList = questionService.getReviewQuestions();
		return ResponseEntity.created(new URI("/api/questions/reviewquestions"))
				.headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ENTITY_NAME))
				.body(userQuestionList);
	}

	/**
	 * 
	 * @param questionDTO
	 * @return questionDTO
	 * @throws Exception
	 */
	@PutMapping("/questions/{userId}/validatequestion")
	public ResponseEntity<QuestionDTO> reviewQuestions(@PathVariable("userId") String userId,
			@Valid @RequestBody QuestionDTO questionDTO) throws Exception {
		log.debug("REST request to get Validate Question {},{}", userId, questionDTO);
		QuestionDTO updateObject = questionService.reviewQuestion(userId, questionDTO);
		return ResponseEntity.created(new URI("/api/questions/validateQuestion"))
				.headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userId))
				.body(updateObject);
	}

	/**
	 * 
	 * @param questionDTO
	 * @return questionDTO
	 * @throws Exception
	 */
	@PostMapping("/questions/{questionId}/responses")
	public ResponseEntity<QuestionDTO> createResponse(@PathVariable("questionId") String questionId,
			@Valid @RequestBody QuestionResponsesDTO responseDTO) throws Exception {
		log.debug("REST request to get create Response for Question {},{}", questionId, responseDTO);
		QuestionDTO updateObject = questionService.createQuestionResponse(questionId, responseDTO);
		return ResponseEntity.created(new URI("/api/questions/reviewedquestions"))
				.headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, questionId))
				.body(updateObject);
	}

	/**
	 * 
	 * @param questionDTO
	 * @return questionDTO
	 * @throws Exception
	 */
	@GetMapping("/questions/{questionId}/responses")
	public ResponseEntity<List<QuestionResponsesDTO>> getQuestionResponse(
			@PathVariable("questionId") String questionId) throws Exception {
		log.debug("REST request to get Get Response for Question {}", questionId);
		List<QuestionResponsesDTO> responses = questionService.getQuestionResponse(questionId);
		return ResponseEntity.created(new URI("/api/questions/reviewedquestions"))
				.headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, questionId))
				.body(responses);
	}

	/**
	 * 
	 * @param questionDTO
	 * @return questionDTO
	 * @throws Exception
	 */
	@PutMapping("/questions/{question-id}/responses/{userId}/validateresponses")
	public ResponseEntity<QuestionDTO> validateResponse(@PathVariable("question-id") String questionId,
			@PathVariable("userId") String userId,
			@Valid @RequestBody QuestionResponsesDTO responseDTO) throws Exception {
		responseDTO.setReviewerId(userId);
		log.debug("REST request to get Validate Response for Question {},{}", questionId, responseDTO);
		QuestionDTO updateObject = questionService.validateQuestionResponse(questionId, responseDTO);
		return ResponseEntity.created(new URI("/api/questions/reviewedquestions"))
				.headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, questionId))
				.body(updateObject);
	}
}
