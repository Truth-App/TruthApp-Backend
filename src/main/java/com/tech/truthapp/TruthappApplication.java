package com.tech.truthapp;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TruthappApplication {

	/** The Constant SPRING_PROFILE_DEVELOPMENT. */
	private static final String SPRING_PROFILE_DEVELOPMENT = "dev";

	/** The Constant SPRING_PROFILE_DEFAULT. */
	private static final String SPRING_PROFILE_DEFAULT = "spring.profiles.default";

	
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(TruthappApplication.class);
		addDefaultProfile(app);
		app.run(args);
	}

	/**
	 * Adds the default profile.
	 *
	 * @param app the app
	 */
	private static void addDefaultProfile(SpringApplication app) {
		Map<String, Object> defProperties = new HashMap<>();
		defProperties.put(SPRING_PROFILE_DEFAULT, SPRING_PROFILE_DEVELOPMENT);
		app.setDefaultProperties(defProperties);
	}

}
