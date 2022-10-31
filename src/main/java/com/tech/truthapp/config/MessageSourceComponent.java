package com.tech.truthapp.config;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class MessageSourceComponent {

	/** The request. */
	@Autowired
	HttpServletRequest request;

	/** The env. */
	@Autowired
	Environment env;

	/** The message source. */
	@Autowired
	MessageSource messageSource;

	/**
	 * Gets the accept language.
	 *
	 * @return the accept language
	 */
	public String getAcceptLanguage() {
		return request.getHeader("Accept-Language");
	}

	public String getLocale() {
		String acceptLanguage = getAcceptLanguage();
		if (acceptLanguage == null) {
			acceptLanguage = "en";
		}
		return acceptLanguage;
	}

	/**
	 * Resolve I 18 n.
	 *
	 * @param code the code
	 * @return the string
	 */
	public String resolveI18n(String code) {
		String acceptLanguage = getAcceptLanguage();
		String message = null;
		if (acceptLanguage != null) {
			Locale locale = new Locale(acceptLanguage);
			message = messageSource.getMessage(code, null, locale);
		} else {
			message = messageSource.getMessage(code, null, Locale.ENGLISH);
		}

		return message;
	}
}
