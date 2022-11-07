package com.tech.truthapp.question.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tech.truthapp.config.MessageSourceComponent;
import com.tech.truthapp.dto.question.QuestionDTO;
import com.tech.truthapp.dto.question.ValidateQuestionDTO;
import com.tech.truthapp.exception.BadRequestException;
import com.tech.truthapp.util.Util;

@Component
public class QuestionValidator {

	/** The Constant ENTITY_NAME. */
	private static final String ENTITY_NAME = "Question";

	@Autowired
	private MessageSourceComponent messageSourceComponent;
	

	public void validateCreateQuestion(QuestionDTO questionDTO) {

		if (questionDTO.getId() != null) {
			String message = "Question.id.nullValue";
			String errorMsg = messageSourceComponent.resolveI18n(message);
			throw new BadRequestException(errorMsg, ENTITY_NAME, "nullValue");
		}
	}

	public void validationOnValidateQuestion(ValidateQuestionDTO questionDTO) {

		if (questionDTO.getId() == null) {
			String message = "Question.id.notNull";
			String errorMsg = messageSourceComponent.resolveI18n(message);
			throw new BadRequestException(errorMsg, ENTITY_NAME, "notNull");
		}
		String tagName = questionDTO.getTagName();
		String tagId = questionDTO.getTagId();
		if (Util.isEmptyString(tagName) && Util.isEmptyString(tagId)) {
			String message = "Question.tagNameOrTagId.notNull";
			String errorMsg = messageSourceComponent.resolveI18n(message);
			throw new BadRequestException(errorMsg, ENTITY_NAME, "notNull");
		}
		
	}
}
