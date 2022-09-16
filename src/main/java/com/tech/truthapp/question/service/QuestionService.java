package com.tech.truthapp.question.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tech.truthapp.dto.QuestionDTO;
import com.tech.truthapp.model.Question;
import com.tech.truthapp.question.mapper.QuestionMapper;
import com.tech.truthapp.question.repository.QuestionRepository;

@Service
@Transactional
public class QuestionService {

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private QuestionMapper questionMapper;

	public QuestionDTO saveQuestion(QuestionDTO questionDTO) {
		Question question = questionMapper.toEntity(questionDTO);
		question.setIsPublic(Boolean.FALSE);
		question.setIsApproved(Boolean.FALSE);
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

	public QuestionDTO reviewQuestion(String userId, QuestionDTO questionDTO) {
		Optional<Question> optionalQuestion = questionRepository.findById(questionDTO.getId());
		if (optionalQuestion.isPresent()) {
			Question dbQuestion = optionalQuestion.get();
			dbQuestion.setReviewerId(userId);
			dbQuestion.setIsPublic(Boolean.TRUE);
			dbQuestion.setIsApproved(Boolean.TRUE);
			dbQuestion.setCategory(questionDTO.getCategory());
			questionRepository.save(dbQuestion);
			QuestionDTO dtoObject = questionMapper.toDto(dbQuestion);
			return dtoObject;
		} else {
			// throw exception
		}
		return null;
	}
}
