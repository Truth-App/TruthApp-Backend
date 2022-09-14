package com.tech.truthapp.question.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tech.truthapp.exception.HeaderUtil;

import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api")
@Log4j2
@Api(tags = "Test Controller", value = "Test Controller", description = "Test Controller")
public class TestController {

	@GetMapping("/test")
	public ResponseEntity<String> testRestURL() throws Exception {

		log.debug("REST request to testRestURL");

		return ResponseEntity.created(new URI("/api/test/"))
				.headers(HeaderUtil.createEntityCreationAlert("", true, "", "")).body("Hello World ");
	}
}
