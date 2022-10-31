package com.tech.truthapp.category.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tech.truthapp.audit.category.CategoryAuditService;
import com.tech.truthapp.audit.category.SubCategoryAuditService;
import com.tech.truthapp.category.mapper.CategoryMapper;
import com.tech.truthapp.category.mapper.SubCategoryMapper;
import com.tech.truthapp.dto.category.CategoryDTO;
import com.tech.truthapp.dto.category.SubCategoryDTO;
import com.tech.truthapp.model.tag.Category;
import com.tech.truthapp.model.tag.SubCategory;

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
public class CategoryService {

	@Autowired
	private ElasticsearchClient elasticsearchClient;

	private final String indexName = "category";

	@Autowired
	private CategoryAuditService categoryAuditService;
	
	@Autowired
	private SubCategoryAuditService subCategoryAuditService;
	
	@Autowired
	private CategoryMapper categoryMapper;
	
	@Autowired
	private SubCategoryMapper subCategoryMapper;
	
	
	/***
	 * 
	 * @param categoryDTO
	 * @return
	 * @throws Exception
	 */
	public CategoryDTO saveCategory(CategoryDTO categoryDTO) throws Exception {
		log.info("Save Category {} ", categoryDTO);
		Category category = categoryMapper.toEntity(categoryDTO);
		category.setId(UUID.randomUUID().toString());
		categoryAuditService.updateAuditOnCreate(category);
		IndexResponse response = elasticsearchClient
				.index(i -> i.index(indexName).id(category.getId()).document(category));
		if (response.result().name().equals("Created")) {

		} else {
			throw new Exception("Exception here");
		}
		categoryDTO = categoryMapper.toDto(category);
		return categoryDTO;
	}
	
	/**
	 * 
	 * @param categoryDTO
	 * @return
	 * @throws Exception
	 */
	public CategoryDTO upateCategory(CategoryDTO categoryDTO) throws Exception {
		log.info("Update Category {} ", categoryDTO);
		String id = categoryDTO.getId();
		Query categoryIdQuery = MatchQuery.of(m -> m 
				.field("id").query(id))._toQuery();
		
		SearchResponse<Category> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(categoryIdQuery) 
	                )
	            ),
	            Category.class
	        );
		
		List<Hit<Category>> hits = response.hits().hits();
		if (hits.isEmpty()) {
			throw new Exception("There is no record found for category id " + categoryDTO.getId());
		}
		Hit<Category> hitObject = hits.get(0);
		Category category = hitObject.source();	
		category.setCategory(categoryDTO.getCategory());
		categoryAuditService.updateAuditOnUpdate(category);
		IndexResponse indexResponse = elasticsearchClient
				.index(i -> i.index(indexName).id(category.getId()).document(category));
		if (indexResponse.result().name().equals("Updated")) {

		} else {
			throw new Exception("Exception here");
		}
		categoryDTO = categoryMapper.toDto(category);
		return categoryDTO;
	}
	/**
	 * 
	 * @param categoryId
	 * @return
	 * @throws Exception
	 */
	public CategoryDTO getCategory(String categoryId) throws Exception {
		log.info("Get Category {} ", categoryId);
		Query categoryIdQuery = MatchQuery.of(m -> m 
				.field("id").query(categoryId))._toQuery();
		
		SearchResponse<Category> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(categoryIdQuery) 
	                )
	            ),
	            Category.class
	        );
		List<Hit<Category>> hits = response.hits().hits();
		if (hits.isEmpty()) {
			throw new Exception("There is no record found for category id " + categoryId);
		}
		Hit<Category> hitObject = hits.get(0);
		CategoryDTO categoryDTO = categoryMapper.toDto(hitObject.source());
		return categoryDTO;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<CategoryDTO> getAllCategory() throws Exception {
		log.info("Get All Category ");
		SearchResponse<Category> response = elasticsearchClient.search(s -> s
	            .index(indexName)	            ,
	            Category.class
	        );
		List<Hit<Category>> hits = response.hits().hits();
		List<Category> dbList = new ArrayList<>();
		for (Hit<Category> hit : hits) {
			Category category = hit.source();
			dbList.add(category);
		}
		List<CategoryDTO> dtoList = categoryMapper.toDto(dbList);
		return dtoList;
	}
	
	/**
	 * 
	 * @param categoryId
	 * @throws Exception
	 */
	public void deleteCategory(String categoryId) throws Exception {
		log.info("delete Category {} ", categoryId);
		DeleteRequest request = DeleteRequest.of(d -> d.index(indexName).id(categoryId));
		DeleteResponse deleteResponse = elasticsearchClient.delete(request);
		log.info(deleteResponse.result().toString());
	}
	
	/**
	 * 
	 * @param categoryId
	 * @param subCategoryDTO
	 * @return
	 * @throws Exception
	 */
	public CategoryDTO saveSubCategory(String categoryId, SubCategoryDTO subCategoryDTO) throws Exception {
		log.info("save SubCategory  {} ", categoryId);
		Query categoryIdQuery = MatchQuery.of(m -> m 
				.field("id").query(categoryId))._toQuery();
		
		SearchResponse<Category> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(categoryIdQuery) 
	                )
	            ),
	            Category.class
	        );
		List<Hit<Category>> hits = response.hits().hits();
		if (hits.isEmpty()) {
			throw new Exception("There is no record found for category id " + categoryId);
		}
		Hit<Category> hitObject = hits.get(0);
		Category category = hitObject.source();
		SubCategory subCategory = subCategoryMapper.toEntity(subCategoryDTO);
		subCategory.setId(UUID.randomUUID().toString());
		subCategory.setCategoryId(categoryId);
		subCategoryAuditService.updateAuditOnCreate(subCategory);
		category.getSubCategoryList().add(subCategory);
		IndexResponse indexResponse = elasticsearchClient
				.index(i -> i.index(indexName).id(category.getId()).document(category));
		if (indexResponse.result().name().equals("Updated")) {

		} else {
			throw new Exception("Exception here");
		}
		CategoryDTO categoryDTO = categoryMapper.toDto(category);
		return categoryDTO;
	}
	
	/**
	 * 
	 * @param categoryId
	 * @param subCategoryDTO
	 * @return
	 * @throws Exception
	 */
	public CategoryDTO updateSubCategory(String categoryId, SubCategoryDTO subCategoryDTO) throws Exception {
		Query categoryIdQuery = MatchQuery.of(m -> m 
				.field("id").query(categoryId))._toQuery();
		
		SearchResponse<Category> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(categoryIdQuery) 
	                )
	            ),
	            Category.class
	        );
		List<Hit<Category>> hits = response.hits().hits();
		if (hits.isEmpty()) {
			throw new Exception("There is no record found for category id " + categoryId);
		}
		Hit<Category> hitObject = hits.get(0);
		Category category = hitObject.source();
		
		Optional<SubCategory> matchingObject = category.getSubCategoryList().stream()
				.filter(object -> object.getId() != null && object.getId().equalsIgnoreCase(subCategoryDTO.getId()))
				.findFirst();
		if (matchingObject.isEmpty()) {
			throw new Exception("There is no record found for sub category id " + subCategoryDTO.getCategoryId());
		}
		SubCategory subCategory = matchingObject.get();
		subCategory.setSubCategory(subCategoryDTO.getSubCategory());
		subCategoryAuditService.updateAuditOnUpdate(subCategory);
		IndexResponse indexResponse = elasticsearchClient
				.index(i -> i.index(indexName).id(category.getId()).document(category));
		if (indexResponse.result().name().equals("Updated")) {

		} else {
			throw new Exception("Exception here");
		}
		CategoryDTO categoryDTO = categoryMapper.toDto(category);
		return categoryDTO;
	}
	
	/**
	 * 
	 * @param categoryId
	 * @return
	 * @throws Exception
	 */
	public List<SubCategoryDTO> getAllSubCategory(String categoryId) throws Exception {
		Query categoryIdQuery = MatchQuery.of(m -> m 
				.field("id").query(categoryId))._toQuery();
		
		SearchResponse<Category> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(categoryIdQuery) 
	                )
	            ),
	            Category.class
	        );
		List<Hit<Category>> hits = response.hits().hits();
		if (hits.isEmpty()) {
			throw new Exception("There is no record found for category id " + categoryId);
		}
		Hit<Category> hitObject = hits.get(0);
		Category category = hitObject.source();
		List<SubCategoryDTO> list = subCategoryMapper.toDto(category.getSubCategoryList());
		return list;
	}
	
	/**
	 * 
	 * @param categoryId
	 * @param subCategoryId
	 * @return
	 * @throws Exception
	 */
	public SubCategoryDTO getSubCategory(String categoryId, String subCategoryId) throws Exception {
		Query categoryIdQuery = MatchQuery.of(m -> m 
				.field("id").query(categoryId))._toQuery();
		
		SearchResponse<Category> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(categoryIdQuery) 
	                )
	            ),
	            Category.class
	        );
		List<Hit<Category>> hits = response.hits().hits();
		if (hits.isEmpty()) {
			throw new Exception("There is no record found for category id " + categoryId);
		}
		Hit<Category> hitObject = hits.get(0);
		Category category = hitObject.source();
		Optional<SubCategory> matchingObject = category.getSubCategoryList().stream()
				.filter(object -> object.getId() != null && object.getId().equalsIgnoreCase(subCategoryId))
				.findFirst();
		if (matchingObject.isEmpty()) {
			throw new Exception("There is no record found for sub category id " + subCategoryId);
		}
		SubCategory subCategory = matchingObject.get();
		return subCategoryMapper.toDto(subCategory);
	}
	
	/**
	 * 
	 * @param categoryId
	 * @param subCategoryId
	 * @throws Exception
	 */
	public void deleteSubCategory(String categoryId, String subCategoryId) throws Exception {
		Query categoryIdQuery = MatchQuery.of(m -> m 
				.field("id").query(categoryId))._toQuery();
		
		SearchResponse<Category> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(categoryIdQuery) 
	                )
	            ),
	            Category.class
	        );
		List<Hit<Category>> hits = response.hits().hits();
		if (hits.isEmpty()) {
			throw new Exception("There is no record found for category id " + categoryId);
		}
		Hit<Category> hitObject = hits.get(0);
		Category category = hitObject.source();
		Optional<SubCategory> matchingObject = category.getSubCategoryList().stream()
				.filter(object -> object.getId() != null && object.getId().equalsIgnoreCase(subCategoryId))
				.findFirst();
		if (matchingObject.isEmpty()) {
			throw new Exception("There is no record found for sub category id " + subCategoryId);
		}
		category.getSubCategoryList().removeIf(object -> object.getId() != null && object.getId().equalsIgnoreCase(subCategoryId));
		categoryAuditService.updateAuditOnUpdate(category);
		IndexResponse indexResponse = elasticsearchClient
				.index(i -> i.index(indexName).id(category.getId()).document(category));
		if (indexResponse.result().name().equals("Updated")) {

		} else {
			throw new Exception("Exception here");
		}
	}
}
