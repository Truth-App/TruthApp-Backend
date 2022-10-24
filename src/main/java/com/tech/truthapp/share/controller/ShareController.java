package com.tech.truthapp.share.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tech.truthapp.share.validation.ShareValidator;

import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api")
@Log4j2
@Api(tags = "Share Controller", value = "Share Controller", description = "Share Controller")
public class ShareController {

	/** The Constant ENTITY_NAME. */
	private static final String ENTITY_NAME = "Share";

	@Value("${truth-app.clientApp.name}")
	private String applicationName;

	@Autowired
	private ShareValidator shareValidator;

	
}
