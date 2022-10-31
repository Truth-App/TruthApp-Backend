package com.tech.truthapp.prayer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tech.truthapp.audit.prayer.PrayerAuditService;
import com.tech.truthapp.audit.prayer.PrayerResponseAuditService;
import com.tech.truthapp.audit.prayer.PrayerResponseReviewAuditService;
import com.tech.truthapp.audit.prayer.PrayerReviewAuditService;
import com.tech.truthapp.dto.prayer.PrayerDTO;
import com.tech.truthapp.dto.prayer.PrayerResponsesDTO;
import com.tech.truthapp.model.prayer.Prayer;
import com.tech.truthapp.model.prayer.PrayerResponse;
import com.tech.truthapp.model.prayer.PrayerResponseReviewer;
import com.tech.truthapp.model.prayer.PrayerReviewer;
import com.tech.truthapp.prayer.mapper.PrayerMapper;
import com.tech.truthapp.prayer.mapper.PrayerResponseMapper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NestedQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PrayerService {

	@Autowired
	private ElasticsearchClient elasticsearchClient;

	private final String indexName = "prayer";

	@Autowired
	private PrayerMapper prayerMapper;
	
	@Autowired
	private PrayerAuditService prayerAuditService;
	
	@Autowired
	private PrayerResponseAuditService prayerResponseAuditService;
	
	@Autowired
	private PrayerReviewAuditService prayerReviewAuditService;

	@Autowired
	private PrayerResponseReviewAuditService prayerResponseReviewAuditService;
	
	@Autowired
	private PrayerResponseMapper prayerResponseMapper;
	
	
	public PrayerDTO savePrayer(PrayerDTO prayerDTO) throws Exception {
		Prayer prayer = prayerMapper.toEntity(prayerDTO);
		prayer.setIsApproved(Boolean.FALSE);
		prayer.setScore(2L);
		prayer.setId(UUID.randomUUID().toString());
		prayer.setStatus("OPEN");
		prayerAuditService.updateAuditOnCreate(prayer);
		IndexResponse response = elasticsearchClient
				.index(i -> i.index(indexName).id(prayer.getId()).document(prayer));
		if (response.result().name().equals("Created")) {
			log.info("Successfully Created");
		} else {
			throw new Exception("Exception while creation of Prayer Request");
		}
		prayerDTO = prayerMapper.toDto(prayer);
		return prayerDTO;
	}	

	public List<PrayerDTO> getReviewedPrayers() throws Exception {
		Boolean isPublic = true;
		Boolean isApproved = true;
		Query isPublicQuery = MatchQuery.of(m -> m 
				.field("isPublic").query(isPublic))._toQuery();
		
		Query isApprovedQuery = MatchQuery.of(m -> m 
				.field("isApproved").query(isApproved))._toQuery();
		

		 // Combine isPublic and isApproved queries to search the prayer index
        SearchResponse<Prayer> response = elasticsearchClient.search(s -> s
            .index(indexName)
            .query(q -> q
                .bool(b -> b 
                    .must(isPublicQuery, isApprovedQuery)
                )
            ),
            Prayer.class
        );
		
		List<Hit<Prayer>> hits = response.hits().hits();
		List<Prayer> dbList = new ArrayList<>();
		for (Hit<Prayer> hit : hits) {
			Prayer prayer = hit.source();
			dbList.add(prayer);
		}
		List<PrayerDTO> dtoList = prayerMapper.toDto(dbList);
		return dtoList;
	}
	/**
	 * 'isApproved' : false, 'score' : {$gt : 0} }"
	 * @return
	 * @throws Exception
	 */
	public List<PrayerDTO> getReviewPrayers() throws Exception {
		Integer maxScore = 0;
		Boolean isApproved = false;
		Query scoreQuery = RangeQuery.of(r -> r 
				.field("score").gt(JsonData.of(maxScore)))._toQuery();
		
		Query isApprovedQuery = MatchQuery.of(m -> m 
				.field("isApproved").query(isApproved))._toQuery();
		

		 // Combine isPublic and isApproved queries to search the prayer index
        SearchResponse<Prayer> response = elasticsearchClient.search(s -> s
            .index(indexName)
            .query(q -> q
                .bool(b -> b 
                    .must(scoreQuery) 
                    .must(isApprovedQuery)
                )
            ),
            Prayer.class
        );
		
		List<Hit<Prayer>> hits = response.hits().hits();
		List<Prayer> dbList = new ArrayList<>();
		for (Hit<Prayer> hit : hits) {
			Prayer prayer = hit.source();
			dbList.add(prayer);
		}
		List<PrayerDTO> dtoList = prayerMapper.toDto(dbList);
		return dtoList;
	}
	/**
	 * 
	 * @param userId
	 * @param prayerDTO
	 * @return
	 * @throws Exception
	 */
	public PrayerDTO validatePrayer(String userId, PrayerDTO prayerDTO) throws Exception {

		Query prayerIdQuery = MatchQuery.of(m -> m 
				.field("id").query(prayerDTO.getId()))._toQuery();
		
		SearchResponse<Prayer> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(prayerIdQuery) 
	                )
	            ),
	            Prayer.class
	        );
		List<Hit<Prayer>> hits = response.hits().hits();
		if (hits.isEmpty()) {
			throw new Exception("There is no record found for prayer id " + prayerDTO.getId());
		}
		Hit<Prayer> hitPrayer = hits.get(0);
		Prayer dbObject = hitPrayer.source();
		if (prayerDTO.getIsApproved()) {
			dbObject.setIsApproved(Boolean.TRUE);
		} else {
			dbObject.setIsApproved(Boolean.FALSE);
			Long score = dbObject.getScore();
			score = score - 1L;
			dbObject.setScore(score);
		}
		dbObject.setGroup(prayerDTO.getGroup());
		prayerAuditService.updateAuditOnUpdate(dbObject);
		PrayerReviewer prayerReviewer = new PrayerReviewer();
		prayerReviewer.setReviewerId(userId);
		prayerReviewer.setComments(userId);
		prayerReviewer.setId(UUID.randomUUID().toString());
		prayerReviewAuditService.updateAuditOnCreate(prayerReviewer);
		dbObject.getReviews().add(prayerReviewer);
		
		IndexResponse indexResponse = elasticsearchClient
				.index(i -> i.index(indexName).id(dbObject.getId()).document(dbObject));
		if (indexResponse.result().name().equals("Updated")) {
			log.info("Successfully Updated");
		} else {
			throw new Exception("Exception while updating the Prayer Request");
		}		
		PrayerDTO updatedDTO = prayerMapper.toDto(dbObject);
		return updatedDTO;
	}
	/**
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<PrayerDTO> getAllPrayersByUser(String userId) throws Exception {
		Integer maxScore = 0;
		Query scoreQuery = RangeQuery.of(r -> r 
				.field("score").gt(JsonData.of(maxScore)))._toQuery();	
		Query userIdQuery = MatchQuery.of(m -> m 
				.field("createdBy").query(userId))._toQuery();
		
		SearchResponse<Prayer> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(userIdQuery) 
	                    .must(scoreQuery) 
	                )
	            ),
	            Prayer.class
	        );
		List<Hit<Prayer>> hits = response.hits().hits();
		List<Prayer> dbList = new ArrayList<>();
		for (Hit<Prayer> hit : hits) {
			Prayer prayer = hit.source();
			dbList.add(prayer);
		}
		List<PrayerDTO> dtoList = prayerMapper.toDto(dbList);
		return dtoList;
	}
	/**
	 * 
	 * @param category
	 * @return
	 * @throws Exception
	 */
	public List<PrayerDTO> getReviewedPrayersForCategory(String category) throws Exception {
		Boolean isApproved = true;
		Boolean isPublic = true;		
		Query isApprovedQuery = MatchQuery.of(m -> m 
				.field("isApproved").query(isApproved))._toQuery();
		Query isPublicQuery = MatchQuery.of(m -> m 
				.field("isPublic").query(isPublic))._toQuery();
		
		Query categoryQuery = MatchQuery.of(m -> m 
				.field("category").query(category))._toQuery();
		
		SearchResponse<Prayer> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                	  .must(categoryQuery)
	 	                  .must(isApprovedQuery) 
	 	                  .must(isPublicQuery) 
	                )
	            ),
	            Prayer.class
	        );
		List<Hit<Prayer>> hits = response.hits().hits();
		List<Prayer> dbList = new ArrayList<>();
		for (Hit<Prayer> hit : hits) {
			Prayer prayer = hit.source();
			dbList.add(prayer);
		}
		List<PrayerDTO> dtoList = prayerMapper.toDto(dbList);
		return dtoList;
	}

	public List<PrayerDTO> getReviewedPrayersByCategoryAndSubCategory(String category, String subCategory) throws Exception {
		Boolean isApproved = true;
		Boolean isPublic = true;		
		Query isApprovedQuery = MatchQuery.of(m -> m 
				.field("isApproved").query(isApproved))._toQuery();
		Query isPublicQuery = MatchQuery.of(m -> m 
				.field("isPublic").query(isPublic))._toQuery();
		
		Query categoryQuery = MatchQuery.of(m -> m 
				.field("category").query(category))._toQuery();
		
		Query subcategoryQuery = MatchQuery.of(m -> m 
				.field("subCategory").query(subCategory))._toQuery();
		
		SearchResponse<Prayer> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(categoryQuery)
	                    .must(subcategoryQuery)
	                    .must(isApprovedQuery) 
	                    .must(isPublicQuery) 
	                )
	            ),
	            Prayer.class
	        );
		List<Hit<Prayer>> hits = response.hits().hits();
		List<Prayer> dbList = new ArrayList<>();
		for (Hit<Prayer> hit : hits) {
			Prayer prayer = hit.source();
			dbList.add(prayer);
		}
		List<PrayerDTO> dtoList = prayerMapper.toDto(dbList);
		return dtoList;
	}
	
	public List<PrayerDTO> getReviewedPrayersByGroup(String group) throws Exception {
		Boolean isApproved = true;
		Boolean isPublic = true;		
		Query isApprovedQuery = MatchQuery.of(m -> m 
				.field("isApproved").query(isApproved))._toQuery();
		Query isPublicQuery = MatchQuery.of(m -> m 
				.field("isPublic").query(isPublic))._toQuery();
		
		Query groupQuery = MatchQuery.of(m -> m 
				.field("group").query(group))._toQuery();
		
		SearchResponse<Prayer> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(groupQuery)
	                    .must(isApprovedQuery) 
	                    .must(isPublicQuery) 
	                )
	            ),
	            Prayer.class
	        );
		List<Hit<Prayer>> hits = response.hits().hits();
		List<Prayer> dbList = new ArrayList<>();
		for (Hit<Prayer> hit : hits) {
			Prayer prayer = hit.source();
			dbList.add(prayer);
		}
		List<PrayerDTO> dtoList = prayerMapper.toDto(dbList);
		return dtoList;
	}
	/**
	 * 
	 * @param prayerId
	 * @param responseDTO
	 * @return
	 * @throws Exception
	 */
	public PrayerDTO createPrayerResponse(String prayerId, PrayerResponsesDTO responseDTO) throws Exception {
		Query prayerIdQuery = MatchQuery.of(m -> m 
				.field("id").query(prayerId))._toQuery();
		
		SearchResponse<Prayer> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(prayerIdQuery) 
	                )
	            ),
	            Prayer.class
	        );
		List<Hit<Prayer>> hits = response.hits().hits();
		if (hits.isEmpty()) {
			throw new Exception("There is no record found for prayer id " + prayerId);
		}
		Hit<Prayer> hitObject = hits.get(0);
		Prayer dbPrayer = hitObject.source();
		PrayerResponse prayerResponse = prayerResponseMapper.toEntity(responseDTO);
		prayerResponse.setResponse(responseDTO.getResponse());
		prayerResponse.setResponderId("system");
		prayerResponse.setId(UUID.randomUUID().toString());		
		prayerResponse.setScore(2L);
		prayerResponse.setIsApproved(Boolean.FALSE);
		prayerResponse.setIsPublic(Boolean.TRUE);
		prayerResponseAuditService.updateAuditOnCreate(prayerResponse);
		prayerAuditService.updateAuditOnUpdate(dbPrayer);
		dbPrayer.getResponses().add(prayerResponse);
		IndexResponse indexResponse = elasticsearchClient
				.index(i -> i.index(indexName).id(dbPrayer.getId()).document(dbPrayer));
		if (indexResponse.result().name().equals("Updated")) {
			log.info("Successfully Updated");
		} else {
			throw new Exception("Exception while updating of Prayer Request");
		}		
		PrayerDTO dtoObject = prayerMapper.toDto(dbPrayer);
		return dtoObject;
	}
	
	public List<PrayerResponsesDTO> getPrayerResponse(String prayerId) throws Exception {
		Query prayerIdQuery = MatchQuery.of(m -> m 
				.field("id").query(prayerId))._toQuery();
		
		SearchResponse<Prayer> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(prayerIdQuery) 
	                )
	            ),
	            Prayer.class
	        );
		List<Hit<Prayer>> hits = response.hits().hits();
		if (hits.isEmpty()) {
			throw new Exception("There is no record found for prayer id " + prayerId);
		}
		Hit<Prayer> hitObject = hits.get(0);
		Prayer dbObject = hitObject.source();
		PrayerDTO dtoObject = prayerMapper.toDto(dbObject);
		return dtoObject.getResponses();
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<PrayerDTO> getMyOutbox(String userId) throws Exception {
		
		Query respondedQuery = MatchQuery.of(m -> m 
				.field("responses.responderId").query(userId))._toQuery();
		
		Query nq = NestedQuery.of(m -> m 
				.path("responses")
				.query(q -> q
					.bool(b -> b.must(respondedQuery))						
						))._toQuery(); 

		SearchResponse<Prayer> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                   // .must(isApprovedQuery,isPublicQuery)
	                    .must(nq)
	                )
	            ),
	            Prayer.class
	        );

		List<Hit<Prayer>> hits = response.hits().hits();		
		List<Prayer> dbList = new ArrayList<>();
		for (Hit<Prayer> hit : hits) {
			Prayer prayer = hit.source();
			prayer.getResponses().removeIf(object -> object.getResponderId() == null || 
					!object.getResponderId().equalsIgnoreCase(userId));
			dbList.add(prayer);
		}
		List<PrayerDTO> dtoList = prayerMapper.toDto(dbList);
		return dtoList;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<PrayerDTO> getReviewResponses() throws Exception {

		  Long maxScore = 0L; 
		  Boolean isApproved = false; 
	     
		Query isApprovedQuery = MatchQuery.of(m -> m 
				.field("responses.isApproved").query(isApproved))._toQuery();
		
		Query scoreQuery = RangeQuery.of(r -> r 
				.field("responses.score").gt(JsonData.of(maxScore)))._toQuery();	
		
		Query nq = NestedQuery.of(m -> m 
				.path("responses")
				.query(q -> q
		                .bool(b -> b 
			                    .must(isApprovedQuery)
			                    .must(scoreQuery)
			            )
				  ))._toQuery();  

		 // Combine isPublic and isApproved queries to search the prayer index
      SearchResponse<Prayer> response = elasticsearchClient.search(s -> s
          .index(indexName)
          .query(nq),
          Prayer.class
      );
		
		List<Hit<Prayer>> hits = response.hits().hits();
		List<Prayer> dbList = new ArrayList<>();
		for (Hit<Prayer> hit : hits) {
			Prayer prayer = hit.source();
			prayer.getResponses().removeIf(object -> object.getIsApproved() && object.getScore() > 0);
			dbList.add(prayer);			
		}
		List<PrayerDTO> dtoList = prayerMapper.toDto(dbList);
		return dtoList;
	}
	
	/**
	 * 
	 * @param prayerd
	 * @param responseDTO
	 * @return
	 * @throws Exception
	 */
	public PrayerDTO validatePrayerResponse(String prayerId, PrayerResponsesDTO responseDTO) throws Exception {
		Query idQuery = MatchQuery.of(m -> m 
				.field("id").query(prayerId))._toQuery();
		
		SearchResponse<Prayer> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(idQuery) 
	                )
	            ),
	            Prayer.class
	        );
		List<Hit<Prayer>> hits = response.hits().hits();
		if (hits.isEmpty()) {
			throw new Exception("There is no record found for prayer " + prayerId);
		}
		Hit<Prayer> hitList = hits.get(0);
		Prayer prayer = hitList.source();
		List<PrayerResponse> responses = prayer.getResponses();

		Optional<PrayerResponse> matchingObject = responses.stream()
				.filter(object -> object.getId() != null && object.getId().equalsIgnoreCase(responseDTO.getId()))
				.findFirst();
		
		if(matchingObject.isEmpty()) {
			throw new Exception("There is no record found for prayer " + prayerId);
		}
		PrayerResponse responseObject = matchingObject.get();
		responseObject.setIsApproved(Boolean.TRUE);
		responseObject.setResponderId("system");
		List<PrayerResponseReviewer> reviewerList = responseObject.getReviews();
		PrayerResponseReviewer prayerReviewer = new PrayerResponseReviewer();
		prayerReviewer.setId(UUID.randomUUID().toString());			
		prayerReviewer.setComments(responseDTO.getComments());
		reviewerList.add(prayerReviewer);
		prayerResponseReviewAuditService.updateAuditOnCreate(prayerReviewer);
		prayerResponseAuditService.updateAuditOnUpdate(responseObject);
		prayerAuditService.updateAuditOnUpdate(prayer);
		IndexResponse indexResponse = elasticsearchClient
				.index(i -> i.index(indexName).id(prayer.getId()).document(prayer));
		if (indexResponse.result().name().equals("Updated")) {
			log.info("Successfully Updated");
		} else {
			throw new Exception("Exception while updating of Prayer Request");
		}
		PrayerDTO dtoObject = prayerMapper.toDto(prayer);
		return dtoObject;
		
	}
	
	/**
	 * @param keyword
	 * @return
	 */
	public List<PrayerDTO> search(String keyword) throws Exception {
		Integer maxScore = 0;
		Boolean isPublic = true;
		Query scoreQuery = RangeQuery.of(r -> r 
				.field("score").gt(JsonData.of(maxScore)))._toQuery();	
		
		Query isPublicQuery = MatchQuery.of(m -> m 
				.field("isPublic").query(isPublic))._toQuery();
		
		Query searchQuery = MatchQuery.of(m -> m 
				.field("search_field")
				.query(keyword))._toQuery();
		
        SearchResponse<Prayer> response = elasticsearchClient.search(s -> s
            .index(indexName)
            .query(q -> q
	                .bool(b -> b 
		                    .must(searchQuery)
		                    .must(isPublicQuery)
		                    .must(scoreQuery)
		                )
		            ),
            Prayer.class
        );
		List<Hit<Prayer>> hits = response.hits().hits();
		List<Prayer> dbList = new ArrayList<>();
		for (Hit<Prayer> hit : hits) {
			Prayer prayer = hit.source();
			dbList.add(prayer);
		}
		List<PrayerDTO> dtoList = prayerMapper.toDto(dbList);
		return dtoList;
	}
	
	/**
	 * 
	 * @param prayerId
	 * @param prayerDTO
	 * @return
	 * @throws Exception
	 */
	public PrayerDTO updatePrayer(String prayerId, PrayerDTO prayerDTO) throws Exception {
		Integer maxScore = 0;
		Query scoreQuery = RangeQuery.of(r -> r 
				.field("score").gt(JsonData.of(maxScore)))._toQuery();
		Query prayerIdQuery = MatchQuery.of(m -> m 
				.field("id").query(prayerId))._toQuery();		
		 Boolean isApproved = false;
		Query isApprovedQuery = MatchQuery.of(m -> m 
					.field("isApproved").query(isApproved))._toQuery();
		
		SearchResponse<Prayer> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(prayerIdQuery)
	                    .must(isApprovedQuery)
	                    .must(scoreQuery)
	                )
	            ),
	            Prayer.class
	        );
		List<Hit<Prayer>> hits = response.hits().hits();
		if (hits.isEmpty()) {
			throw new Exception("There is no record found for prayer id " + prayerId);
		}
		Hit<Prayer> hitPrayer = hits.get(0);
		Prayer prayer = hitPrayer.source();
		prayer.setPrayer(prayerDTO.getPrayer());
		prayer.setIsPublic(prayerDTO.getIsPublic());
		prayer.setCategory(prayerDTO.getCategory());
		prayer.setSubCategory(prayerDTO.getSubCategory());
		prayerAuditService.updateAuditOnUpdate(prayer);
		
		IndexResponse indexResponse = elasticsearchClient
				.index(i -> i.index(indexName).id(prayer.getId()).document(prayer));
		if (indexResponse.result().name().equals("Updated")) {
			log.info("Successfully Updated Prayer Object");
		} else {
			throw new Exception("Exception while updating of Prayer Request");
		}		
		PrayerDTO dtoObject = prayerMapper.toDto(prayer);
		return dtoObject;
	}
	
	
	public PrayerDTO deletePrayer(String prayerId) throws Exception {
		Query prayerIdQuery = MatchQuery.of(m -> m 
				.field("id").query(prayerId))._toQuery();		
		 Boolean isApproved = false;
		Query isApprovedQuery = MatchQuery.of(m -> m 
					.field("isApproved").query(isApproved))._toQuery();
		
		SearchResponse<Prayer> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(prayerIdQuery)
	                    .must(isApprovedQuery)
	                )
	            ),
	            Prayer.class
	        );
		List<Hit<Prayer>> hits = response.hits().hits();
		if (hits.isEmpty()) {
			throw new Exception("There is no record found for Prayer id " + prayerId);
		}
		Hit<Prayer> hitPrayer = hits.get(0);
		Prayer dbPrayer = hitPrayer.source();
		dbPrayer.setScore(-1L);
		prayerAuditService.updateAuditOnUpdate(dbPrayer);
		IndexResponse indexResponse = elasticsearchClient
				.index(i -> i.index(indexName).id(dbPrayer.getId()).document(dbPrayer));
		if (indexResponse.result().name().equals("Updated")) {
			log.info("Successfully Updated ");
		} else {
			throw new Exception("Exception while updating of Prayer Request");
		}		
		PrayerDTO dtoObject = prayerMapper.toDto(dbPrayer);
		return dtoObject;
	}
	
	public PrayerDTO updateStatus(String prayerId) throws Exception {
		Query idQuery = MatchQuery.of(m -> m 
				.field("id").query(prayerId))._toQuery();		
		 Boolean isApproved = false;
		Query isApprovedQuery = MatchQuery.of(m -> m 
					.field("isApproved").query(isApproved))._toQuery();
		
		SearchResponse<Prayer> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(idQuery)
	                    .must(isApprovedQuery)
	                )
	            ),
	            Prayer.class
	        );
		List<Hit<Prayer>> hits = response.hits().hits();
		if (hits.isEmpty()) {
			throw new Exception("There is no record found for prayer id " + prayerId);
		}
		Hit<Prayer> hitObject = hits.get(0);
		Prayer dbObject = hitObject.source();
		dbObject.setStatus("CLOSED");
		prayerAuditService.updateAuditOnUpdate(dbObject);
		IndexResponse indexResponse = elasticsearchClient
				.index(i -> i.index(indexName).id(dbObject.getId()).document(dbObject));
		if (indexResponse.result().name().equals("Updated")) {
			log.info("Successfully Updated Object");
		} else {
			throw new Exception("Exception while updating of Prayer Request");
		}
		PrayerDTO dtoObject = prayerMapper.toDto(dbObject);
		return dtoObject;
	}
	
	public List<PrayerDTO> getAllPrayers() {		
		List<Prayer> prayerList = null; //prayerRepository.findAll();
		List<PrayerDTO> list = prayerMapper.toDto(prayerList);
		return list;
	}
}
