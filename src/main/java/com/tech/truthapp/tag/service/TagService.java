package com.tech.truthapp.tag.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tech.truthapp.audit.tag.TagAuditService;
import com.tech.truthapp.dto.tag.TagDTO;
import com.tech.truthapp.model.tag.Tag;
import com.tech.truthapp.tag.mapper.TagMapper;
import com.tech.truthapp.util.Util;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TagService {

	@Autowired
	private ElasticsearchClient elasticsearchClient;

	private final String indexName = "tag";
	
	@Autowired
	private TagAuditService tagAuditService;

	@Autowired
	private TagMapper tagMapper;
	
	
	/**
	 * 
	 * @param tagDTO
	 * @return
	 * @throws Exception
	 */
	public TagDTO saveTag(TagDTO tagDTO) throws Exception {
		Tag tag  = tagMapper.toEntity(tagDTO);
		tag.setId(UUID.randomUUID().toString());
		tagAuditService.updateAuditOnCreate(tag);
		IndexResponse response = elasticsearchClient
				.index(i -> i.index(indexName).id(tag.getId()).document(tag));
		if (response.result().name().equals("Created")) {
			log.info("Successfully Created");
		} else {
			throw new Exception("Exception while creation of tag request");
		}
		tagDTO = tagMapper.toDto(tag);
		return tagDTO;
	}
	
	public TagDTO saveTagWithQuestion(TagDTO tagDTO) throws Exception {
		Tag tag  = tagMapper.toEntity(tagDTO);
		tag.setId(UUID.randomUUID().toString());
		tagAuditService.updateAuditOnCreate(tag);
		IndexResponse response = elasticsearchClient
				.index(i -> i.index(indexName).id(tag.getId()).document(tag));
		if (response.result().name().equals("Created")) {
			log.info("Successfully Created");
		} else {
			throw new Exception("Exception while creation of tag request");
		}
		tagDTO = tagMapper.toDto(tag);
		return tagDTO;
	}
	
	public TagDTO updateTag(String tagId, TagDTO tagDTO) throws Exception {
		
		Query idQuery = MatchQuery.of(m -> m 
				.field("id").query(tagId))._toQuery();		
		
		SearchResponse<Tag> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(idQuery)
	                )
	            ),
	            Tag.class
	        );
		List<Hit<Tag>> hits = response.hits().hits();
		if (hits.isEmpty()) {
			throw new Exception("There is no record found for tag id " + tagId);
		}
		Hit<Tag> hitObject = hits.get(0);
		Tag tagObject = hitObject.source();
		tagObject.setTagType(tagDTO.getTagType());
		tagObject.setCategory(tagDTO.getCategory());
		tagObject.setSubCategory(tagDTO.getSubCategory());
		tagAuditService.updateAuditOnUpdate(tagObject);
		IndexResponse indexResponse = elasticsearchClient
				.index(i -> i.index(indexName).id(tagObject.getId()).document(tagObject));
		if (indexResponse.result().name().equals("Updated")) {
			log.info("Successfully Updated Object");
		} else {
			throw new Exception("Exception while updation of tag request");
		}		
		TagDTO dtoObject = tagMapper.toDto(tagObject);
		return dtoObject;
	}
	
	public TagDTO addQuestion(String tagId, List<String> questionList) throws Exception {
		log.info("Add Question to Tag {}, {} ", tagId , questionList);
		Query idQuery = MatchQuery.of(m -> m 
				.field("id").query(tagId))._toQuery();		
		
		SearchResponse<Tag> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(idQuery)
	                )
	            ),
	            Tag.class
	        );
		List<Hit<Tag>> hits = response.hits().hits();
		if (hits.isEmpty()) {
			throw new Exception("There is no record found for tag id " + tagId);
		}
		Hit<Tag> hitObject = hits.get(0);
		Tag tagObject = hitObject.source();

		List<String> availableList = tagObject.getSubList();
		availableList.addAll(questionList);
		List<String> updatedList = new ArrayList<String>(
			      new HashSet<String>(availableList));
		tagObject.setSubList(updatedList);
		tagAuditService.updateAuditOnUpdate(tagObject);
		IndexResponse indexResponse = elasticsearchClient
				.index(i -> i.index(indexName).id(tagObject.getId()).document(tagObject));
		if (indexResponse.result().name().equals("Updated")) {
			log.info("Successfully Updated Object");
		} else {
			throw new Exception("Exception while updation of tag request");
		}		
		TagDTO dtoObject = tagMapper.toDto(tagObject);
		return dtoObject;
	}
	
	/**
	 * 
	 * @param keyword
	 * @return
	 * @throws Exception
	 */
	public List<TagDTO> search(String tagType, String keyword) throws Exception {
		log.info("Search keywords {}, {} ", tagType, keyword );
		Query searchQuery = MatchQuery.of(m -> m 
				.field("search_field")
				.query(keyword))._toQuery();
		
		Query tagTypeQuery = MatchQuery.of(m -> m 
				.field("tagType")
				.query(tagType))._toQuery();
		
        SearchResponse<Tag> response = elasticsearchClient.search(s -> s
            .index(indexName)
            .query(q -> q
	                .bool(b -> b 
	                		 .must(tagTypeQuery)
		                     .must(searchQuery)
		                )
		            ),
            Tag.class
        );
		List<Tag> tagList = response.hits()
				                    .hits()
				                    .stream()
				                    .map(Hit::source)
				                    .collect(Collectors.toList());
		
		List<TagDTO> dtoList = tagMapper.toDto(tagList);
		return dtoList;
	}
	
	/**
	 * 
	 * @param tagType
	 * @param category
	 * @param subCategory
	 * @param group
	 * @return
	 * @throws Exception
	 */
	public List<TagDTO> filterData(String tagType, String category, String subCategory, String group) throws Exception {
		log.info("REST request to get filterData {}, {}, {}, {} ", tagType, category, subCategory, group);
	
        SearchResponse<Tag> response = null;
        if (!Util.isEmptyString(category) && !Util.isEmptyString(subCategory) && !Util.isEmptyString(group)) {
        	response = getResultsByCategorySubCategoryGroup(tagType, category, subCategory, group);
        } else if (!Util.isEmptyString(category) && !Util.isEmptyString(subCategory)) {
        	response = getResultsByCategorySubCategory(tagType, category, subCategory);
        } else if (!Util.isEmptyString(category)) {
        	response = getResultsByCategory(tagType, category);
        } else {
        	response = getResultsByTagType(tagType);
        }
		List<Tag> tagList = response.hits()
				                    .hits()
				                    .stream()
				                    .map(Hit::source)
				                    .collect(Collectors.toList());
		
		List<TagDTO> dtoList = tagMapper.toDto(tagList);
		return dtoList;
	}
	
	private SearchResponse<Tag> getResultsByCategorySubCategoryGroup(String tagType, String category, 
				String subCategory, String group) throws Exception {
		
		Query tagTypeQuery = MatchQuery.of(m -> m 
				.field("tagType")
				.query(tagType))._toQuery();
		
		Query categoryQuery = MatchQuery.of(m -> m 
				.field("category")
				.query(category))
				._toQuery();
		
		Query subCategoryQuery = MatchQuery.of(m -> m 
				.field("subCategory")
				.query(subCategory))
				._toQuery();
		
		Query groupQuery = MatchQuery.of(m -> m 
				.field("group")
				.query(group))
				._toQuery();
		
		SearchResponse<Tag> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
		                .bool(b -> b 
			                    .must(tagTypeQuery)
			                    .must(categoryQuery)
			                    .must(subCategoryQuery)
			                    .must(groupQuery)
			                )
			            ),
	            Tag.class
	        );
		return response;
	}
	
	private SearchResponse<Tag> getResultsByCategorySubCategory(String tagType, String category, 
			String subCategory) throws Exception {
	
		Query tagTypeQuery = MatchQuery.of(m -> m 
				.field("tagType")
				.query(tagType))._toQuery();
		
		Query categoryQuery = MatchQuery.of(m -> m 
				.field("category")
				.query(category))
				._toQuery();
		
		Query subCategoryQuery = MatchQuery.of(m -> m 
				.field("subCategory")
				.query(subCategory))
				._toQuery();
		
		
	
	SearchResponse<Tag> response = elasticsearchClient.search(s -> s
            .index(indexName)
            .query(q -> q
	                .bool(b -> b 
		                    .must(tagTypeQuery)
		                    .must(categoryQuery)
		                    .must(subCategoryQuery)
		                )
		            ),
            Tag.class
        );
	return response;
  }
	
	private SearchResponse<Tag> getResultsByCategory(String tagType, String category 
			) throws Exception {
	
		Query tagTypeQuery = MatchQuery.of(m -> m 
				.field("tagType")
				.query(tagType))._toQuery();
		
		Query categoryQuery = MatchQuery.of(m -> m 
				.field("category")
				.query(category))
				._toQuery();
	
	SearchResponse<Tag> response = elasticsearchClient.search(s -> s
            .index(indexName)
            .query(q -> q
	                .bool(b -> b 
		                    .must(tagTypeQuery)
		                    .must(categoryQuery)
		                )
		            ),
            Tag.class
        );
	return response;
  }
	
	private SearchResponse<Tag> getResultsByTagType(String tagType) throws Exception {
	
		Query tagTypeQuery = MatchQuery.of(m -> m 
				.field("tagType")
				.query(tagType))._toQuery();
		
		
	
	SearchResponse<Tag> response = elasticsearchClient.search(s -> s
            .index(indexName)
            .query(q -> q
	                .bool(b -> b 
		                    .must(tagTypeQuery)
		                )
		            ),
            Tag.class
        );
	return response;
  }
}
