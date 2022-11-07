package com.tech.truthapp.registration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tech.truthapp.config.MessageSourceComponent;
import com.tech.truthapp.registration.service.UserService;

import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/v1/api")
@Log4j2
@Api(tags = "User Registration Controller", value = "User Registration Controller", description = "User Registration Controller")
public class UserRegistrationController {

	@Autowired
	private UserService userService;

	@Autowired
	private MessageSourceComponent messageSourceComponent;

	/** The Constant ENTITY_NAME. */
	private static final String ENTITY_NAME = "User";

	@Value("${truth-app.clientApp.name}")
	private String applicationName;

}
