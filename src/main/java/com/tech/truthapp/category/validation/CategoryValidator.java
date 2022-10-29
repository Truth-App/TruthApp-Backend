package com.tech.truthapp.category.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tech.truthapp.config.MessageSourceComponent;
import com.tech.truthapp.dto.category.CategoryDTO;
import com.tech.truthapp.exception.BadRequestException;

@Component
public class CategoryValidator {

	/** The Constant ENTITY_NAME. */
	private static final String ENTITY_NAME = "Category";

	@Autowired
	private MessageSourceComponent messageSourceComponent;

	public void validateCreateCategory(CategoryDTO categoryDTO) {

		if (categoryDTO.getId() != null) {
			String message = "category.id.nullValue";
			String errorMsg = messageSourceComponent.resolveI18n(message);
			throw new BadRequestException(errorMsg, ENTITY_NAME, "nullValue");
		}
	}

}
