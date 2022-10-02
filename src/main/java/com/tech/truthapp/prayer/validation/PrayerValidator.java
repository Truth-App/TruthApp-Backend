package com.tech.truthapp.prayer.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tech.truthapp.config.MessageSourceComponent;
import com.tech.truthapp.dto.PrayerDTO;
import com.tech.truthapp.exception.BadRequestException;

@Component
public class PrayerValidator {

	/** The Constant ENTITY_NAME. */
	private static final String ENTITY_NAME = "Prayer";

	@Autowired
	private MessageSourceComponent messageSourceComponent;

	public void validateCreatePrayer(PrayerDTO questionDTO) {

		if (questionDTO.getId() != null) {
			String message = "Prayer.id.nullValue";
			String errorMsg = messageSourceComponent.resolveI18n(message);
			throw new BadRequestException(errorMsg, ENTITY_NAME, "nullValue");
		}
	}
}
