package com.tech.truthapp.exception;

import java.net.URI;

// TODO: Auto-generated Javadoc
/**
 * The Class ErrorConstants.
 */
public final class ErrorConstants {
	
	/** The Constant PROBLEM_BASE_URL. */
	public static final String PROBLEM_BASE_URL = "https://abc.com/problem";
	/** The Constant DEFAULT_TYPE. */
	public static final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
	/** The Constant CONSTRAINT_VIOLATION_TYPE. */
	public static final URI CONSTRAINT_VIOLATION_TYPE = URI.create(PROBLEM_BASE_URL + "/constraint-violation");
	/** The Constant INTERNAL_SERVER_ERROR_TYPE. */
	public static final URI INTERNAL_SERVER_ERROR_TYPE = URI.create(PROBLEM_BASE_URL + "/internal-server-error");

	/**
	 * Instantiates a new error constants.
	 */
	private ErrorConstants() {
	}
}
