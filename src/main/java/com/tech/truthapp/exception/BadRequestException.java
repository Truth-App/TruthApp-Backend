package com.tech.truthapp.exception;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

// TODO: Auto-generated Javadoc
/**
 * The Class BadRequestAlertException.
 */
public class BadRequestException extends AbstractThrowableProblem {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The entity name. */
	private final String entityName;

	/** The error key. */
	private final String errorKey;

	/**
	 * Instantiates a new bad request alert exception.
	 *
	 * @param defaultMessage the default message
	 * @param entityName the entity name
	 * @param errorKey the error key
	 */
	public BadRequestException(String defaultMessage, String entityName, String errorKey) {
		this(ErrorConstants.DEFAULT_TYPE, defaultMessage, entityName, errorKey);
	}

	/**
	 * Instantiates a new bad request alert exception.
	 *
	 * @param type the type
	 * @param defaultMessage the default message
	 * @param entityName the entity name
	 * @param errorKey the error key
	 */
	public BadRequestException(URI type, String defaultMessage, String entityName, String errorKey) {
		super(type, defaultMessage, Status.BAD_REQUEST, null, null, null, getAlertParameters(entityName, errorKey));
		this.entityName = entityName;
		this.errorKey = errorKey;
	}

	/**
	 * Gets the entity name.
	 *
	 * @return the entity name
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * Gets the error key.
	 *
	 * @return the error key
	 */
	public String getErrorKey() {
		return errorKey;
	}

	/**
	 * Gets the alert parameters.
	 *
	 * @param entityName the entity name
	 * @param errorKey the error key
	 * @return the alert parameters
	 */
	private static Map<String, Object> getAlertParameters(String entityName, String errorKey) {
		Map<String, Object> parameters = new HashMap<>();
		// parameters.put("responseObject", responseDTO);
		// parameters.put("message", "error." + errorKey);
		return parameters;
	}
}
