package com.tech.truthapp.share.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tech.truthapp.audit.share.ShareAuditService;
import com.tech.truthapp.audit.share.ShareReviewAuditService;
import com.tech.truthapp.dto.share.ShareDTO;
import com.tech.truthapp.model.share.Share;
import com.tech.truthapp.model.share.ShareReviewer;
import com.tech.truthapp.share.mapper.ShareMapper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ShareService {

	@Autowired
	private ElasticsearchClient elasticsearchClient;

	private final String indexName = "share";
	
	@Autowired
	private ShareAuditService shareAuditService;

	@Autowired
	private ShareMapper shareMapper;
	
	@Autowired
	private ShareReviewAuditService shareReviewAuditService;
	
	/**
	 * 
	 * @param shareDTO
	 * @return
	 * @throws Exception
	 */
	public ShareDTO saveShare(ShareDTO shareDTO) throws Exception {
		Share share  = shareMapper.toEntity(shareDTO);
		share.setIsPublic(Boolean.TRUE);
		share.setIsApproved(Boolean.FALSE);
		share.setScore(2L);
		share.setId(UUID.randomUUID().toString());
		shareAuditService.updateAuditOnCreate(share);
		IndexResponse response = elasticsearchClient
				.index(i -> i.index(indexName).id(share.getId()).document(share));
		if (response.result().name().equals("Created")) {
			log.info("Successfully Created");
		} else {
			throw new Exception("Exception here");
		}
		shareDTO = shareMapper.toDto(share);
		return shareDTO;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ShareDTO> getReviewedShares() throws Exception {
		Boolean isPublic = true;
		Boolean isApproved = true;
		Query isPublicQuery = MatchQuery.of(m -> m 
				.field("isPublic").query(isPublic))._toQuery();
		
		Query isApprovedQuery = MatchQuery.of(m -> m 
				.field("isApproved").query(isApproved))._toQuery();
		

		 // Combine isPublic and isApproved queries to search the share index
        SearchResponse<Share> response = elasticsearchClient.search(s -> s
            .index(indexName)
            .query(q -> q
                .bool(b -> b 
                    .must(isPublicQuery, isApprovedQuery)
                )
            ),
            Share.class
        );
		
		List<Hit<Share>> hits = response.hits().hits();
		List<Share> dbList = new ArrayList<>();
		for (Hit<Share> hit : hits) {
			Share share = hit.source();
			dbList.add(share);
		}
		List<ShareDTO> dtoList = shareMapper.toDto(dbList);
		return dtoList;
	}
	
	public List<ShareDTO> getReviewShares() throws Exception {
		Integer maxScore = 0;
		Boolean isApproved = false;
		Query scoreQuery = RangeQuery.of(r -> r 
				.field("score").gt(JsonData.of(maxScore)))._toQuery();
		
		Query isApprovedQuery = MatchQuery.of(m -> m 
				.field("isApproved").query(isApproved))._toQuery();
		

		 // Combine isPublic and isApproved queries to search the share index
        SearchResponse<Share> response = elasticsearchClient.search(s -> s
            .index(indexName)
            .query(q -> q
                .bool(b -> b 
                    .must(scoreQuery) 
                    .must(isApprovedQuery)
                )
            ),
            Share.class
        );
		
		List<Hit<Share>> hits = response.hits().hits();
		List<Share> dbList = new ArrayList<>();
		for (Hit<Share> hit : hits) {
			Share share = hit.source();
			dbList.add(share);
		}
		List<ShareDTO> dtoList = shareMapper.toDto(dbList);
		return dtoList;
	}
	
	public ShareDTO validateShare(String userId, ShareDTO shareDTO) throws Exception {
		String id = shareDTO.getId();
		Query idQuery = MatchQuery.of(m -> m 
				.field("id").query(id))._toQuery();
		
		SearchResponse<Share> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(idQuery) 
	                )
	            ),
	            Share.class
	        );
		List<Hit<Share>> hits = response.hits().hits();
		if (hits.isEmpty()) {
			throw new Exception("There is no record found for share id " + shareDTO.getId());
		}
		Hit<Share> hitObject = hits.get(0);
		Share share = hitObject.source();
		if (shareDTO.getIsApproved()) {
			share.setIsApproved(Boolean.TRUE);
		} else {
			share.setIsApproved(Boolean.FALSE);
			Long score = share.getScore();
			score = score - 1L;
			share.setScore(score);
		}
		share.setGroup(shareDTO.getGroup());
		ShareReviewer reviewer = new ShareReviewer();
		reviewer.setReviewerId(userId);
		reviewer.setComments(userId);
		reviewer.setId(UUID.randomUUID().toString());
		shareReviewAuditService.updateAuditOnCreate(reviewer);
		share.getReviews().add(reviewer);
		shareAuditService.updateAuditOnUpdate(share);
		
		IndexResponse indexResponse = elasticsearchClient
				.index(i -> i.index(indexName).id(share.getId()).document(share));
		if (indexResponse.result().name().equals("Updated")) {
			log.info("Successfully Updated");
		} else {
			throw new Exception("Exception here");
		}		
		shareDTO = shareMapper.toDto(share);
		return shareDTO;
	}
	
	public List<ShareDTO> getAllSharesByUser(String userId) throws Exception {
		Integer maxScore = 0;
		Query scoreQuery = RangeQuery.of(r -> r 
				.field("score").gt(JsonData.of(maxScore)))._toQuery();	
		Query userIdQuery = MatchQuery.of(m -> m 
				.field("createdBy").query(userId))._toQuery();
		
		SearchResponse<Share> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(userIdQuery) 
	                    .must(scoreQuery)
	                )
	            ),
	            Share.class
	        );
		List<Hit<Share>> hits = response.hits().hits();
		List<Share> dbList = new ArrayList<>();
		for (Hit<Share> hit : hits) {
			Share share = hit.source();
			dbList.add(share);
		}
		List<ShareDTO> dtoList = shareMapper.toDto(dbList);
		return dtoList;
	}
	
	public List<ShareDTO> getReviewedSharesByCategory(String category) throws Exception {
		Boolean isApproved = true;
		Boolean isPublic = true;		
		Query isApprovedQuery = MatchQuery.of(m -> m 
				.field("isApproved").query(isApproved))._toQuery();
		Query isPublicQuery = MatchQuery.of(m -> m 
				.field("isPublic").query(isPublic))._toQuery();
		
		Query categoryQuery = MatchQuery.of(m -> m 
				.field("category").query(category))._toQuery();
		
		SearchResponse<Share> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(categoryQuery)
	                    .must(isApprovedQuery) 
	                    .must(isPublicQuery) 
	                )
	            ),
	            Share.class
	        );
		List<Hit<Share>> hits = response.hits().hits();
		List<Share> dbList = new ArrayList<>();
		for (Hit<Share> hit : hits) {
			Share share = hit.source();
			dbList.add(share);
		}
		List<ShareDTO> dtoList = shareMapper.toDto(dbList);
		return dtoList;
	}

	public List<ShareDTO> getReviewedSharesByCategoryAndSubCategory(String category, String subCategory) throws Exception {
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
		
		SearchResponse<Share> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(categoryQuery)
	                    .must(subcategoryQuery)
	                    .must(isApprovedQuery) 
	                    .must(isPublicQuery) 
	                )
	            ),
	            Share.class
	        );
		List<Hit<Share>> hits = response.hits().hits();
		List<Share> dbList = new ArrayList<>();
		for (Hit<Share> hit : hits) {
			Share share = hit.source();
			dbList.add(share);
		}
		List<ShareDTO> dtoList = shareMapper.toDto(dbList);
		return dtoList;
	}
	
	public List<ShareDTO> getReviewedSharesByGroup(String group) throws Exception {
		Boolean isApproved = true;
		Boolean isPublic = true;		
		Query isApprovedQuery = MatchQuery.of(m -> m 
				.field("isApproved").query(isApproved))._toQuery();
		Query isPublicQuery = MatchQuery.of(m -> m 
				.field("isPublic").query(isPublic))._toQuery();
		
		Query groupQuery = MatchQuery.of(m -> m 
				.field("group").query(group))._toQuery();
		
		SearchResponse<Share> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(groupQuery)
	                    .must(isApprovedQuery) 
	                    .must(isPublicQuery) 
	                )
	            ),
	            Share.class
	        );
		List<Hit<Share>> hits = response.hits().hits();
		List<Share> dbList = new ArrayList<>();
		for (Hit<Share> hit : hits) {
			Share share = hit.source();
			dbList.add(share);
		}
		List<ShareDTO> dtoList = shareMapper.toDto(dbList);
		return dtoList;
	}
	
	public List<ShareDTO> search(String keyword) throws Exception {
		Integer maxScore = 0;
		Boolean isPublic = true;
		Query scoreQuery = RangeQuery.of(r -> r 
				.field("score").gt(JsonData.of(maxScore)))._toQuery();	
		
		Query isPublicQuery = MatchQuery.of(m -> m 
				.field("isPublic").query(isPublic))._toQuery();
		
		Query searchQuery = MatchQuery.of(m -> m 
				.field("search_field")
				.query(keyword))._toQuery();
		
        SearchResponse<Share> response = elasticsearchClient.search(s -> s
            .index(indexName)
            .query(q -> q
	                .bool(b -> b 
		                    .must(searchQuery)
		                    .must(isPublicQuery)
		                    .must(scoreQuery)
		                )
		            ),
            Share.class
        );
		List<Hit<Share>> hits = response.hits().hits();
		List<Share> dbList = new ArrayList<>();
		for (Hit<Share> hit : hits) {
			Share share = hit.source();
			dbList.add(share);
		}
		List<ShareDTO> dtoList = shareMapper.toDto(dbList);
		return dtoList;
	}
	
	
	public ShareDTO updateShare(String shareId, ShareDTO shareDTO) throws Exception {
		Integer maxScore = 0;
		Query scoreQuery = RangeQuery.of(r -> r 
				.field("score").gt(JsonData.of(maxScore)))._toQuery();
		Query idQuery = MatchQuery.of(m -> m 
				.field("id").query(shareId))._toQuery();		
		 Boolean isApproved = false;
		Query isApprovedQuery = MatchQuery.of(m -> m 
					.field("isApproved").query(isApproved))._toQuery();
		
		SearchResponse<Share> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(idQuery)
	                    .must(isApprovedQuery)
	                    .must(scoreQuery)
	                )
	            ),
	            Share.class
	        );
		List<Hit<Share>> hits = response.hits().hits();
		if (hits.isEmpty()) {
			throw new Exception("There is no record found for share id " + shareId);
		}
		Hit<Share> hitObject = hits.get(0);
		Share dbShare = hitObject.source();
		dbShare.setShare(shareDTO.getShare());
		dbShare.setShareType(shareDTO.getShareType());
		dbShare.setCategory(shareDTO.getCategory());
		dbShare.setSubCategory(shareDTO.getSubCategory());
		shareAuditService.updateAuditOnUpdate(dbShare);
		IndexResponse indexResponse = elasticsearchClient
				.index(i -> i.index(indexName).id(dbShare.getId()).document(dbShare));
		if (indexResponse.result().name().equals("Updated")) {
			log.info("Successfully Updated Object");
		} else {
			throw new Exception("Exception here");
		}		
		ShareDTO dtoObject = shareMapper.toDto(dbShare);
		return dtoObject;
	}
	
	public ShareDTO deleteShare(String shareId) throws Exception {
		Query idQuery = MatchQuery.of(m -> m 
				.field("id").query(shareId))._toQuery();		
		 Boolean isApproved = false;
		Query isApprovedQuery = MatchQuery.of(m -> m 
					.field("isApproved").query(isApproved))._toQuery();
		
		SearchResponse<Share> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(idQuery)
	                    .must(isApprovedQuery)
	                )
	            ),
	            Share.class
	        );
		List<Hit<Share>> hits = response.hits().hits();
		if (hits.isEmpty()) {
			throw new Exception("There is no record found for share id " + shareId);
		}
		Hit<Share> hitObject = hits.get(0);
		Share dbshare = hitObject.source();
		dbshare.setScore(-1L);
		shareAuditService.updateAuditOnUpdate(dbshare);
		IndexResponse indexResponse = elasticsearchClient
				.index(i -> i.index(indexName).id(dbshare.getId()).document(dbshare));
		if (indexResponse.result().name().equals("Updated")) {
			log.info("Successfully Updated Object");
		} else {
			throw new Exception("Exception here");
		}		
		ShareDTO dtoObject = shareMapper.toDto(dbshare);
		return dtoObject;
	}
	
	
	public ShareDTO updateStatus(String shareId) throws Exception {
		Query idQuery = MatchQuery.of(m -> m 
				.field("id").query(shareId))._toQuery();		
		 Boolean isApproved = false;
		Query isApprovedQuery = MatchQuery.of(m -> m 
					.field("isApproved").query(isApproved))._toQuery();
		
		SearchResponse<Share> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(idQuery)
	                    .must(isApprovedQuery)
	                )
	            ),
	            Share.class
	        );
		List<Hit<Share>> hits = response.hits().hits();
		if (hits.isEmpty()) {
			throw new Exception("There is no record found for share id " + shareId);
		}
		Hit<Share> hitObject = hits.get(0);
		Share dbShare = hitObject.source();
		dbShare.setStatus("CLOSED");
		shareAuditService.updateAuditOnUpdate(dbShare);
		IndexResponse indexResponse = elasticsearchClient
				.index(i -> i.index(indexName).id(dbShare.getId()).document(dbShare));
		if (indexResponse.result().name().equals("Updated")) {
			log.info("Successfully Updated Object");
		} else {
			throw new Exception("Exception here");
		}		
		ShareDTO dtoObject = shareMapper.toDto(dbShare);
		return dtoObject;
	}
	
	
	public List<ShareDTO> getReviewedSharesByShareType(String shareType) throws Exception {
		Boolean isApproved = true;
		Boolean isPublic = true;		
		Query isApprovedQuery = MatchQuery.of(m -> m 
				.field("isApproved").query(isApproved))._toQuery();
		Query isPublicQuery = MatchQuery.of(m -> m 
				.field("isPublic").query(isPublic))._toQuery();
		
		Query groupQuery = MatchQuery.of(m -> m 
				.field("shareType").query(shareType))._toQuery();
		
		SearchResponse<Share> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(groupQuery)
	                    .must(isApprovedQuery) 
	                    .must(isPublicQuery) 
	                )
	            ),
	            Share.class
	        );
		List<Hit<Share>> hits = response.hits().hits();
		List<Share> dbList = new ArrayList<>();
		for (Hit<Share> hit : hits) {
			Share share = hit.source();
			dbList.add(share);
		}
		List<ShareDTO> dtoList = shareMapper.toDto(dbList);
		return dtoList;
	}
}
