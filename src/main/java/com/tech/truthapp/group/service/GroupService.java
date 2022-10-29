package com.tech.truthapp.group.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tech.truthapp.audit.group.GroupAuditService;
import com.tech.truthapp.dto.group.GroupDTO;
import com.tech.truthapp.group.mapper.GroupMapper;
import com.tech.truthapp.model.group.Group;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class GroupService {

	@Autowired
	private ElasticsearchClient elasticsearchClient;

	private final String indexName = "group";

	@Autowired
	private GroupAuditService groupAuditService;
	
	@Autowired
	private GroupMapper groupMapper;

	/**
	 * 
	 * @param groupDTO
	 * @return
	 * @throws Exception
	 */
	public GroupDTO saveGroup(GroupDTO groupDTO) throws Exception {
		log.info("Save Group {} ", groupDTO);
		groupDTO.setId(UUID.randomUUID().toString());
		Group group = groupMapper.toEntity(groupDTO);
		groupAuditService.updateAuditOnCreate(group);
		IndexResponse response = elasticsearchClient
				.index(i -> i.index(indexName).id(group.getId()).document(group));
		if (response.result().name().equals("Created")) {
			log.info("Successfully Created Group with name {} ", group.getGroup());
		} else {
			throw new Exception("Exception here");
		}
		groupDTO = groupMapper.toDto(group);
		return groupDTO;
	}
	
	/**
	 * 
	 * @param groupDTO
	 * @return
	 * @throws Exception
	 */
	public GroupDTO upateGroup(GroupDTO groupDTO) throws Exception {
		log.info("Update Group {} ", groupDTO);
		String id = groupDTO.getId();
		Query categoryIdQuery = MatchQuery.of(m -> m 
				.field("id").query(id))._toQuery();
		
		SearchResponse<Group> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(categoryIdQuery) 
	                )
	            ),
	            Group.class
	        );
		
		List<Hit<Group>> hits = response.hits().hits();
		if (hits.isEmpty()) {
			throw new Exception("There is no record found for group name " + groupDTO.getGroup());
		}
		Hit<Group> hitObject = hits.get(0);
		Group group = hitObject.source();	
		group.setGroup(groupDTO.getGroup());
		groupAuditService.updateAuditOnUpdate(group);
		IndexResponse indexResponse = elasticsearchClient
				.index(i -> i.index(indexName).id(group.getId()).document(group));
		if (indexResponse.result().name().equals("Updated")) {

		} else {
			throw new Exception("Exception here");
		}
		groupDTO = groupMapper.toDto(group);
		return groupDTO;
	}
	
	/**
	 * 
	 * @param group
	 * @return
	 * @throws Exception
	 */
	public GroupDTO getGroup(String group) throws Exception {
		log.info("Get Group {} ", group);
		Query categoryIdQuery = MatchQuery.of(m -> m 
				.field("group").query(group))._toQuery();
		
		SearchResponse<Group> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(categoryIdQuery) 
	                )
	            ),
	            Group.class
	        );
		List<Hit<Group>> hits = response.hits().hits();
		if (hits.isEmpty()) {
			throw new Exception("There is no record found for group name " + group);
		}
		Hit<Group> hitObject = hits.get(0);
		GroupDTO groupDTO = groupMapper.toDto(hitObject.source());
		return groupDTO;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<GroupDTO> getAllGroups() throws Exception {
		log.info("Get All Groups ");
		SearchResponse<Group> response = elasticsearchClient.search(s -> s
	            .index(indexName)	            ,
	            Group.class
	        );
		List<Hit<Group>> hits = response.hits().hits();
		List<Group> dbList = new ArrayList<Group>();
		for (Hit<Group> hit : hits) {
			Group group = hit.source();
			dbList.add(group);
		}
		List<GroupDTO> dtoList = groupMapper.toDto(dbList);
		return dtoList;
	}
	
	/**
	 * 
	 * @param groupId
	 * @throws Exception
	 */
	public void deleteGroup(String groupId) throws Exception {
		log.info("delete Group {} ", groupId);
		DeleteRequest request = DeleteRequest.of(d -> d.index(indexName).id(groupId));
		DeleteResponse deleteResponse = elasticsearchClient.delete(request);
		log.info(deleteResponse.result().toString());
	}
}
