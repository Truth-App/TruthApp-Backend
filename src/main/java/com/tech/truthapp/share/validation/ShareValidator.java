package com.tech.truthapp.share.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tech.truthapp.config.MessageSourceComponent;
import com.tech.truthapp.dto.share.ShareDTO;
import com.tech.truthapp.exception.BadRequestException;

@Component
public class ShareValidator {

	/** The Constant ENTITY_NAME. */
	private static final String ENTITY_NAME = "Share";

	@Autowired
	private MessageSourceComponent messageSourceComponent;

	public void validateCreate(ShareDTO sareDTO) {

		if (sareDTO.getId() != null) {
			String message = "Share.id.nullValue";
			String errorMsg = messageSourceComponent.resolveI18n(message);
			throw new BadRequestException(errorMsg, ENTITY_NAME, "nullValue");
		}
	}
}
