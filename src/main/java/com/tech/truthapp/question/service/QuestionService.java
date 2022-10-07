package com.tech.truthapp.question.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tech.truthapp.dto.QuestionDTO;
import com.tech.truthapp.dto.QuestionResponsesDTO;
import com.tech.truthapp.model.Question;
import com.tech.truthapp.model.QuestionResponse;
import com.tech.truthapp.model.QuestionResponseReviewer;
import com.tech.truthapp.model.QuestionReviewer;
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
	
	public List<QuestionDTO> getReviewedQuestionsForCategory(String category) {
		List<Question> userQuestionList = questionRepository.findByReviewedQuestionsForCategory(category);
		List<QuestionDTO> dtoList = questionMapper.toDto(userQuestionList);
		return dtoList;
	}

	public List<QuestionDTO> getReviewQuestions() {
		List<Question> userQuestionList = questionRepository.findByReviewQuestions();
		List<QuestionDTO> dtoList = questionMapper.toDto(userQuestionList);
		return dtoList;
	}

	public QuestionDTO reviewQuestion(String userId, QuestionDTO questionDTO) {
		Optional<Question> optionalQuestion = questionRepository.findByquestionForReview(questionDTO.getId());
				
		if (optionalQuestion.isPresent()) {
			Question dbQuestion = optionalQuestion.get();
			//dbQuestion.setIsPublic(questionDTO.getIsPublic());
			if (questionDTO.getIsApproved()) {
				dbQuestion.setIsApproved(Boolean.TRUE);
				dbQuestion.setCategory(questionDTO.getCategory());
			} else {
				dbQuestion.setIsApproved(Boolean.FALSE);
				Integer score = dbQuestion.getScore();
				score = score - 1;
				dbQuestion.setScore(score);
			}
			QuestionReviewer questionReviewer = new QuestionReviewer();
			questionReviewer.setReviewerId(userId);
			questionReviewer.setComments(userId);
			questionReviewer.setId(new ObjectId().toString());
			dbQuestion.getReviews().add(questionReviewer);
			questionRepository.save(dbQuestion);
			QuestionDTO dtoObject = questionMapper.toDto(dbQuestion);
			return dtoObject;
		} else {
			System.out.println("Yes in side else condition");
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
			response.setResponderId("system");
			response.setId(new ObjectId().toString());
			response.setCreatedBy("system");
			response.setCreatedAt(new Date());
			response.setIsApproved(Boolean.FALSE);
			response.setScore(2);
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
					.filter(object -> object.getId() != null && object.getId().equalsIgnoreCase(responseDTO.getId()))
					.findFirst();

			if (matchingObject.isPresent()) {
				QuestionResponse responseObject = matchingObject.get();
				responseObject.setIsApproved(Boolean.TRUE);
				List<QuestionResponseReviewer> reviewerList = responseObject.getReviews();
				QuestionResponseReviewer questionReviewer = new QuestionResponseReviewer();				
				questionReviewer.setComments(responseDTO.getComments());
				questionReviewer.setReviewerId(responseDTO.getReviewerId());
				reviewerList.add(questionReviewer);				
				questionRepository.save(dbQuestion);
				QuestionDTO dtoObject = questionMapper.toDto(dbQuestion);
				return dtoObject;
			} else {
				System.out.println("Yes there is no responses");
				
			}
		} else {
			System.out.println("Else block here");
			// throw exception
		}
		return null;
	}
	
	public List<QuestionDTO> getReviewResponses() {
		List<Question> userQuestionList = questionRepository.findByReviewResponses();
		for (Question question : userQuestionList) {
			question.getResponses().removeIf(object -> object.getIsApproved() && object.getScore() > 0);
		}
		List<QuestionDTO> dtoList = questionMapper.toDto(userQuestionList);
		return dtoList;
	}
	
	public List<QuestionDTO> getMyReviewedResponses(String userId) {
		List<Question> userQuestionList = questionRepository.findByReviewedQuestions();
		for (Question question : userQuestionList) {
			question.getResponses().removeIf(object -> object.getResponderId() == null || 
					!object.getResponderId().equalsIgnoreCase(userId));
		}
		List<QuestionDTO> dtoList = questionMapper.toDto(userQuestionList);
		return dtoList;
	}
}
