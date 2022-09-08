package com.tech.truthapp.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "truth-app", ignoreUnknownFields = false)
public class TruthAppProperties implements InitializingBean {

	private final CorsConfiguration cors = new CorsConfiguration();

	private final ClientApp clientApp = new ClientApp();

	public CorsConfiguration getCors() {
		return cors;
	}

	public ClientApp getClientApp() {
		return clientApp;
	}

	public static class ClientApp {

		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
	}

}
