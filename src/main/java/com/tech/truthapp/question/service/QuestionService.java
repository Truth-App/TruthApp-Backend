package com.tech.truthapp.question.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tech.truthapp.dto.QuestionDTO;
import com.tech.truthapp.dto.QuestionResponsesDTO;
import com.tech.truthapp.model.Question;
import com.tech.truthapp.model.QuestionResponse;
import com.tech.truthapp.question.mapper.QuestionMapper;
import com.tech.truthapp.question.mapper.QuestionResponseMapper;
import com.tech.truthapp.question.repository.QuestionRepository;

@Service
@Transactional
public class QuestionService {

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private QuestionMapper questionMapper;

	@Autowired
	private QuestionResponseMapper questionResponseMapper;

	public QuestionDTO saveQuestion(QuestionDTO questionDTO) {
		Question question = questionMapper.toEntity(questionDTO);
		question.setIsApproved(Boolean.FALSE);
		question.setScore(2);
		questionRepository.save(question);
		questionDTO = questionMapper.toDto(question);
		return questionDTO;
	}

	public List<QuestionDTO> getAllQuestionsByUser(String userId) {
		List<Question> userQuestionList = questionRepository.findQuestionByUserId(userId);
		List<QuestionDTO> dtoList = questionMapper.toDto(userQuestionList);
		return dtoList;
	}

	public List<QuestionDTO> getReviewedQuestions() {
		List<Question> userQuestionList = questionRepository.findByReviewedQuestions();
		List<QuestionDTO> dtoList = questionMapper.toDto(userQuestionList);
		return dtoList;
	}

	public List<QuestionDTO> getReviewQuestions() {
		List<Question> userQuestionList = questionRepository.findByReviewQuestions();
		List<QuestionDTO> dtoList = questionMapper.toDto(userQuestionList);
		return dtoList;
	}

	public QuestionDTO reviewQuestion(String userId, QuestionDTO questionDTO) {
		Optional<Question> optionalQuestion = questionRepository.findByIdAndScoreGreaterThanZero(questionDTO.getId(),
				BigDecimal.ONE);
		if (optionalQuestion.isPresent()) {
			Question dbQuestion = optionalQuestion.get();
			dbQuestion.setReviewerId(userId);
			if (questionDTO.getIsApproved()) {
				dbQuestion.setIsApproved(Boolean.TRUE);
				dbQuestion.setCategory(questionDTO.getCategory());
			} else {
				dbQuestion.setIsApproved(Boolean.FALSE);
				Integer score = dbQuestion.getScore();
				score = score - 1;
				dbQuestion.setScore(score);
			}
			questionRepository.save(dbQuestion);
			QuestionDTO dtoObject = questionMapper.toDto(dbQuestion);
			return dtoObject;
		} else {
			// throw exception
		}
		return null;
	}

	public QuestionDTO createQuestionResponse(String questionId, QuestionResponsesDTO responseDTO) {
		Optional<Question> optionalQuestion = questionRepository.findById(questionId);
		if (optionalQuestion.isPresent()) {
			Question dbQuestion = optionalQuestion.get();
			QuestionResponse response = questionResponseMapper.toEntity(responseDTO);
			response.setResponse(responseDTO.getResponse());
			response.setCreatedBy("system");
			response.setCreatedAt(new Date());
			response.setIsApproved(Boolean.FALSE);
			response.setScore(3);
			response.setLastModifiedBy("system");
			response.setUpdatedAt(new Date());
			dbQuestion.getResponses().add(response);
			questionRepository.save(dbQuestion);
			QuestionDTO dtoObject = questionMapper.toDto(dbQuestion);
			return dtoObject;
		} else {
			// throw exception
		}
		return null;
	}

	public List<QuestionResponsesDTO> getQuestionResponse(String questionId) {
		Optional<Question> optionalQuestion = questionRepository.findById(questionId);
		if (optionalQuestion.isPresent()) {
			Question dbQuestion = optionalQuestion.get();
			QuestionDTO dtoObject = questionMapper.toDto(dbQuestion);
			return dtoObject.getResponses();
		} else {
			// throw exception
		}
		return null;
	}

	public QuestionDTO validateQuestionResponse(String questionId, QuestionResponsesDTO responseDTO) {
		Optional<Question> optionalQuestion = questionRepository.findById(questionId);
		if (optionalQuestion.isPresent()) {
			Question dbQuestion = optionalQuestion.get();
			List<QuestionResponse> responses = dbQuestion.getResponses();

			Optional<QuestionResponse> matchingObject = responses.stream()
					.filter(object -> object.getId().equalsIgnoreCase(responseDTO.getId())).findFirst();

			if (matchingObject.isPresent()) {
				QuestionResponse responseObject = matchingObject.get();
				responseObject.setIsApproved(Boolean.TRUE);
				responseObject.setReviewerId(responseDTO.getReviewerId());
				questionRepository.save(dbQuestion);
				QuestionDTO dtoObject = questionMapper.toDto(dbQuestion);
				return dtoObject;
			}
			return null;
		} else {
			// throw exception
		}
		return null;
	}
}
