package com.tech.truthapp.group.controller;

import java.net.URI;
import java.util.List;

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

import com.tech.truthapp.dto.group.GroupDTO;
import com.tech.truthapp.exception.HeaderUtil;
import com.tech.truthapp.group.service.GroupService;

import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api")
@Log4j2
@Api(tags = "Group Controller", value = "Group Controller", description = "Group Controller")
public class GroupController {

	/** The Constant ENTITY_NAME. */
	private static final String ENTITY_NAME = "Group";

	@Value("${truth-app.clientApp.name}")
	private String applicationName;

	@Autowired
	private GroupService groupService;
	
	
	/**
	 * 
	 * @param groupDTO
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/groups")
	public ResponseEntity<GroupDTO> saveGroup(@Valid @RequestBody GroupDTO groupDTO) throws Exception {
		log.debug("REST request to Save Group {} ", groupDTO);
		groupDTO = groupService.saveGroup(groupDTO);
		return ResponseEntity
				.created(new URI("/api/groups/" + groupDTO.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, groupDTO.getId().toString()))
				.body(groupDTO);
	}
	
	/**
	 * 
	 * @param groupId
	 * @param groupDTO
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/groups/{groupId}")
	public ResponseEntity<GroupDTO> updateGroup(@PathVariable("groupId") String groupId,
			@Valid @RequestBody GroupDTO groupDTO) throws Exception {
		log.debug("REST request to Update Group {}, {} ", groupId, groupDTO);
		groupDTO.setId(groupId);
		groupDTO = groupService.upateGroup(groupDTO);
		return ResponseEntity.created(new URI("/api/group/" + groupId))
				.headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, groupId))
				.body(groupDTO);
	}
	
	/***
	 * 
	 * @param group
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/groups/{groupName}")
	public ResponseEntity<GroupDTO> getGroup(@PathVariable("groupName") String group) throws Exception {
		log.debug("REST request to Get Group {}", group);
		GroupDTO groupDTO = groupService.getGroup(group);
		return ResponseEntity.ok().body(groupDTO);
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/groups")
	public ResponseEntity<List<GroupDTO>> getAllGroups() throws Exception {
		log.debug("REST request to Get All Groups");
		List<GroupDTO> groupList = groupService.getAllGroups();
		return ResponseEntity.ok().body(groupList);
	}
	
	/**
	 * 
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	@DeleteMapping("/groups/{groupId}")
	public ResponseEntity<Void> DeleteGroup(@PathVariable("groupId") String groupId) throws Exception {
		log.debug("REST request to Delete Group {}", groupId);
		groupService.deleteGroup(groupId);
		return ResponseEntity.ok().build();
	}
}
