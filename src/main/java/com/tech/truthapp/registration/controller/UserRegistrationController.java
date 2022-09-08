package com.tech.truthapp.registration.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tech.truthapp.config.MessageSourceComponent;
import com.tech.truthapp.dto.UserDTO;
import com.tech.truthapp.exception.BadRequestException;
import com.tech.truthapp.exception.HeaderUtil;
import com.tech.truthapp.registration.service.UserService;

import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/v1/api")
@Log4j2
@Api(tags = "User Registration Controller", 
	 value = "User Registration Controller", 
	 description = "User Registration Controller")
public class UserRegistrationController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private MessageSourceComponent messageSourceComponent;
	
	/** The Constant ENTITY_NAME. */
	private static final String ENTITY_NAME = "User";

	@Value("${truth-app.clientApp.name}")
	private String applicationName;

	
	/**
	 * 
	 * @param userDTO
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/profiles")
	public ResponseEntity<UserDTO> saveUser( 
			@Valid @RequestBody UserDTO userDTO) throws Exception {
		
		log.debug("REST request to Save User");
		if(userDTO.getUserId() != null) {
			String message = "User.id.nullValue";
			String errorMsg = messageSourceComponent.resolveI18n(message);
			throw new BadRequestException(errorMsg, ENTITY_NAME, "nullValue");
		}
		userDTO = userService.saveUser(userDTO);
		return ResponseEntity
				.created(new URI("/v1/api/profiles" + userDTO.getUserId()))
				.headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
						userDTO.getUserId().toString()))
				.body(userDTO);
	}
	
	/**
	 * 
	 * @param userDTO
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/profiles")
	public ResponseEntity<UserDTO> updateUser( 
			@Valid @RequestBody UserDTO userDTO) throws Exception {
		
		log.debug("REST request to Update User");
		if(userDTO.getUserId() == null) {
			String message = "User.id.nullValue";
			String errorMsg = messageSourceComponent.resolveI18n(message);
			throw new BadRequestException(errorMsg, ENTITY_NAME, "nullValue");
		}
		userDTO = userService.updateUser(userDTO);
		return ResponseEntity
				.created(new URI("/v1/api/profiles" + userDTO.getUserId()))
				.headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
						userDTO.getUserId().toString()))
				.body(userDTO);
	}
	
	/***
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/profiles/{userId}")
	public ResponseEntity<UserDTO> getUser(
			@PathVariable("userId") String userId) throws Exception {
		log.debug("REST request to Get User {0}", userId);
		UserDTO userDTO = userService.getUser(userId);
		return ResponseEntity.ok(userDTO);
	}
	
	/***
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@DeleteMapping("/profiles/{userId}")
	public ResponseEntity<Void> deleteUser(
			@PathVariable("userId") String userId) throws Exception {
		log.debug("REST request to Delete User {0}", userId);
		userService.deleteUser(userId);
		return ResponseEntity.noContent()
				.headers(HeaderUtil
						.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, userId.toString()))
				.build();
	}
}
