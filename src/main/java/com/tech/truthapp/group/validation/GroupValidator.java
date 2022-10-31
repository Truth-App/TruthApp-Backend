package com.tech.truthapp.group.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tech.truthapp.config.MessageSourceComponent;
import com.tech.truthapp.dto.group.GroupDTO;
import com.tech.truthapp.exception.BadRequestException;

@Component
public class GroupValidator {

	/** The Constant ENTITY_NAME. */
	private static final String ENTITY_NAME = "Group";

	@Autowired
	private MessageSourceComponent messageSourceComponent;

	public void validateOnCreate(GroupDTO groupDTO) {

		if (groupDTO.getId() != null) {
			String message = "category.id.nullValue";
			String errorMsg = messageSourceComponent.resolveI18n(message);
			throw new BadRequestException(errorMsg, ENTITY_NAME, "nullValue");
		}
	}

}
