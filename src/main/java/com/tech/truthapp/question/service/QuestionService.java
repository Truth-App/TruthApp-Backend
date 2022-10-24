package com.tech.truthapp.question.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tech.truthapp.audit.question.QuestionAuditService;
import com.tech.truthapp.audit.question.QuestionResponseAuditService;
import com.tech.truthapp.audit.question.QuestionResponseReviewAuditService;
import com.tech.truthapp.audit.question.QuestionReviewAuditService;
import com.tech.truthapp.dto.question.QuestionDTO;
import com.tech.truthapp.dto.question.QuestionResponsesDTO;
import com.tech.truthapp.model.question.Question;
import com.tech.truthapp.model.question.QuestionResponse;
import com.tech.truthapp.model.question.QuestionResponseReviewer;
import com.tech.truthapp.model.question.QuestionReviewer;
import com.tech.truthapp.question.mapper.QuestionMapper;
import com.tech.truthapp.question.mapper.QuestionResponseMapper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NestedQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuestionService {

	@Autowired
	private ElasticsearchClient elasticsearchClient;

	private final String indexName = "question";

	@Autowired
	private QuestionMapper questionMapper;
	
	@Autowired
	private QuestionAuditService questionAuditService;
	
	@Autowired
	private QuestionResponseAuditService questionResponseAuditService;

	@Autowired
	private QuestionResponseReviewAuditService questionResponseReviewAuditService;
	
	@Autowired
	private QuestionReviewAuditService questionReviewAuditService;
	
	@Autowired
	private QuestionResponseMapper questionResponseMapper;
	
	
	public QuestionDTO saveQuestion(QuestionDTO questionDTO) throws Exception {
		Question question = questionMapper.toEntity(questionDTO);
		question.setIsApproved(Boolean.FALSE);
		question.setScore(2L);
		question.setId(UUID.randomUUID().toString());
		questionAuditService.updateQuestionAuditOnCreate(question);
		IndexResponse response = elasticsearchClient
				.index(i -> i.index(indexName).id(question.getId()).document(question));
		if (response.result().name().equals("Created")) {

		} else {
			throw new Exception("Exception here");
		}
		questionDTO = questionMapper.toDto(question);
		return questionDTO;
	}

	public List<QuestionDTO> getReviewedQuestions() throws Exception {
		Boolean isPublic = true;
		Boolean isApproved = true;
		Query isPublicQuery = MatchQuery.of(m -> m 
				.field("isPublic").query(isPublic))._toQuery();
		
		Query isApprovedQuery = MatchQuery.of(m -> m 
				.field("isApproved").query(isApproved))._toQuery();
		

		 // Combine isPublic and isApproved queries to search the question index
        SearchResponse<Question> response = elasticsearchClient.search(s -> s
            .index(indexName)
            .query(q -> q
                .bool(b -> b 
                    .must(isPublicQuery, isApprovedQuery)
                )
            ),
            Question.class
        );
		
		List<Hit<Question>> hits = response.hits().hits();
		List<Question> dbList = new ArrayList<>();
		for (Hit<Question> hit : hits) {
			Question question = hit.source();
			dbList.add(question);
		}
		List<QuestionDTO> dtoList = questionMapper.toDto(dbList);
		return dtoList;
	}
	/**
	 * 'isApproved' : false, 'score' : {$gt : 0} }"
	 * @return
	 * @throws Exception
	 */
	public List<QuestionDTO> getReviewQuestions() throws Exception {
		Integer maxScore = 0;
		Boolean isApproved = false;
		Query scoreQuery = RangeQuery.of(r -> r 
				.field("score").gt(JsonData.of(maxScore)))._toQuery();
		
		Query isApprovedQuery = MatchQuery.of(m -> m 
				.field("isApproved").query(isApproved))._toQuery();
		

		 // Combine isPublic and isApproved queries to search the question index
        SearchResponse<Question> response = elasticsearchClient.search(s -> s
            .index(indexName)
            .query(q -> q
                .bool(b -> b 
                    .must(scoreQuery) 
                    .must(isApprovedQuery)
                )
            ),
            Question.class
        );
		
		List<Hit<Question>> hits = response.hits().hits();
		List<Question> dbList = new ArrayList<>();
		for (Hit<Question> hit : hits) {
			Question question = hit.source();
			dbList.add(question);
		}
		List<QuestionDTO> dtoList = questionMapper.toDto(dbList);
		return dtoList;
	}
	
	public QuestionDTO validateQuestion(String userId, QuestionDTO questionDTO) throws Exception {

		Query questionIdQuery = MatchQuery.of(m -> m 
				.field("id").query(questionDTO.getId()))._toQuery();
		
		SearchResponse<Question> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(questionIdQuery) 
	                )
	            ),
	            Question.class
	        );
		List<Hit<Question>> hits = response.hits().hits();
		if (hits.isEmpty()) {
			throw new Exception("There is no record found for question id " + questionDTO.getId());
		}
		Hit<Question> hitQuestion = hits.get(0);
		Question dbQuestion = hitQuestion.source();
		if (questionDTO.getIsApproved()) {
			dbQuestion.setIsApproved(Boolean.TRUE);
			dbQuestion.setCategory(questionDTO.getCategory());
		} else {
			dbQuestion.setIsApproved(Boolean.FALSE);
			Long score = dbQuestion.getScore();
			score = score - 1L;
			dbQuestion.setScore(score);
		}
		questionAuditService.updateQuestionAuditOnUpdate(dbQuestion);
		QuestionReviewer questionReviewer = new QuestionReviewer();
		questionReviewer.setReviewerId(userId);
		questionReviewer.setComments(userId);
		questionReviewer.setId(UUID.randomUUID().toString());
		questionReviewAuditService.updateQuestionAuditOnCreate(questionReviewer);
		dbQuestion.getReviews().add(questionReviewer);
		
		IndexResponse indexResponse = elasticsearchClient
				.index(i -> i.index(indexName).id(dbQuestion.getId()).document(dbQuestion));
		if (indexResponse.result().name().equals("Updated")) {
			
		} else {
			throw new Exception("Exception here");
		}		
		QuestionDTO updatedQuestionDTO = questionMapper.toDto(dbQuestion);
		return updatedQuestionDTO;
	}
	
	public List<QuestionDTO> getAllQuestionsByUser(String userId) throws Exception {
		Query userIdQuery = MatchQuery.of(m -> m 
				.field("createdBy").query(userId))._toQuery();
		
		SearchResponse<Question> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(userIdQuery) 
	                )
	            ),
	            Question.class
	        );
		List<Hit<Question>> hits = response.hits().hits();
		List<Question> dbList = new ArrayList<>();
		for (Hit<Question> hit : hits) {
			Question question = hit.source();
			dbList.add(question);
		}
		List<QuestionDTO> dtoList = questionMapper.toDto(dbList);
		return dtoList;
	}
	
	public List<QuestionDTO> getReviewedQuestionsForCategory(String category) throws Exception {
		Boolean isApproved = true;
		Boolean isPublic = true;		
		Query isApprovedQuery = MatchQuery.of(m -> m 
				.field("isApproved").query(isApproved))._toQuery();
		Query isPublicQuery = MatchQuery.of(m -> m 
				.field("isPublic").query(isPublic))._toQuery();
		
		Query categoryQuery = MatchQuery.of(m -> m 
				.field("category").query(category))._toQuery();
		
		SearchResponse<Question> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(categoryQuery)
	                    .must(isApprovedQuery) 
	                    .must(isPublicQuery) 
	                )
	            ),
	            Question.class
	        );
		List<Hit<Question>> hits = response.hits().hits();
		List<Question> dbList = new ArrayList<>();
		for (Hit<Question> hit : hits) {
			Question question = hit.source();
			dbList.add(question);
		}
		List<QuestionDTO> dtoList = questionMapper.toDto(dbList);
		return dtoList;
	}

	public QuestionDTO createQuestionResponse(String questionId, QuestionResponsesDTO responseDTO) throws Exception {
		Query questionIdQuery = MatchQuery.of(m -> m 
				.field("id").query(questionId))._toQuery();
		
		SearchResponse<Question> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(questionIdQuery) 
	                )
	            ),
	            Question.class
	        );
		List<Hit<Question>> hits = response.hits().hits();
		if (hits.isEmpty()) {
			throw new Exception("There is no record found for question id " + questionId);
		}
		Hit<Question> hitQuestion = hits.get(0);
		Question dbQuestion = hitQuestion.source();
		QuestionResponse questionResponse = questionResponseMapper.toEntity(responseDTO);
		questionResponse.setResponse(responseDTO.getResponse());
		questionResponse.setResponderId("system");
		questionResponse.setId(UUID.randomUUID().toString());		
		questionResponse.setScore(2L);
		questionResponse.setIsApproved(Boolean.FALSE);
		questionResponseAuditService.updateQuestionAuditOnCreate(questionResponse);
		questionAuditService.updateQuestionAuditOnUpdate(dbQuestion);
		dbQuestion.getResponses().add(questionResponse);
		IndexResponse indexResponse = elasticsearchClient
				.index(i -> i.index(indexName).id(dbQuestion.getId()).document(dbQuestion));
		if (indexResponse.result().name().equals("Updated")) {
			
		} else {
			throw new Exception("Exception here");
		}		
		QuestionDTO dtoObject = questionMapper.toDto(dbQuestion);
		return dtoObject;
	}
	
	public List<QuestionResponsesDTO> getQuestionResponse(String questionId) throws Exception {
		Query questionIdQuery = MatchQuery.of(m -> m 
				.field("id").query(questionId))._toQuery();
		
		SearchResponse<Question> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(questionIdQuery) 
	                )
	            ),
	            Question.class
	        );
		List<Hit<Question>> hits = response.hits().hits();
		if (hits.isEmpty()) {
			throw new Exception("There is no record found for question id " + questionId);
		}
		Hit<Question> hitQuestion = hits.get(0);
		Question dbQuestion = hitQuestion.source();
		QuestionDTO dtoObject = questionMapper.toDto(dbQuestion);
		return dtoObject.getResponses();
	}
	
	/**
	 * @param userId
	 * @return
	 */
	public List<QuestionDTO> getMyOutbox(String userId) throws Exception {
		Query respondedQuery = MatchQuery.of(m -> m 
				.field("responses.responderId").query(userId))._toQuery();
		
		Query nq = NestedQuery.of(m -> m 
				.path("responses")
				.query(q -> q
					.bool(b -> b.must(respondedQuery))						
						))._toQuery(); 

		SearchResponse<Question> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                   // .must(isApprovedQuery,isPublicQuery)
	                    .must(nq)
	                )
	            ),
	            Question.class
	        );

		List<Hit<Question>> hits = response.hits().hits();		
		List<Question> dbList = new ArrayList<>();
		for (Hit<Question> hit : hits) {
			Question question = hit.source();
			question.getResponses().removeIf(object -> object.getResponderId() == null || 
					!object.getResponderId().equalsIgnoreCase(userId));
			dbList.add(question);
		}
		List<QuestionDTO> dtoList = questionMapper.toDto(dbList);
		return dtoList;
	}
	
	
	public List<QuestionDTO> getReviewResponses() throws Exception {
		
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

		 // Combine isPublic and isApproved queries to search the question index
        SearchResponse<Question> response = elasticsearchClient.search(s -> s
            .index(indexName)
            .query(nq),
            Question.class
        );
		
		List<Hit<Question>> hits = response.hits().hits();
		List<Question> dbList = new ArrayList<>();
		for (Hit<Question> hit : hits) {
			Question question = hit.source();
			question.getResponses().removeIf(object -> object.getIsApproved() && object.getScore() > 0);
			dbList.add(question);			
		}		
		List<QuestionDTO> dtoList = questionMapper.toDto(dbList);
		return dtoList;
	}
	
	public QuestionDTO validateQuestionResponse(String questionId, QuestionResponsesDTO responseDTO) throws Exception {
		Query questionIdQuery = MatchQuery.of(m -> m 
				.field("id").query(questionId))._toQuery();
		
		SearchResponse<Question> response = elasticsearchClient.search(s -> s
	            .index(indexName)
	            .query(q -> q
	                .bool(b -> b 
	                    .must(questionIdQuery) 
	                )
	            ),
	            Question.class
	        );
		List<Hit<Question>> hits = response.hits().hits();
		if (hits.isEmpty()) {
			throw new Exception("There is no record found for question id " + questionId);
		}
		Hit<Question> hitQuestion = hits.get(0);
		Question dbQuestion = hitQuestion.source();
		List<QuestionResponse> responses = dbQuestion.getResponses();

		Optional<QuestionResponse> matchingObject = responses.stream()
				.filter(object -> object.getId() != null && object.getId().equalsIgnoreCase(responseDTO.getId()))
				.findFirst();

		if (matchingObject.isPresent()) {
			QuestionResponse responseObject = matchingObject.get();
			responseObject.setIsApproved(Boolean.TRUE);			
			List<QuestionResponseReviewer> reviewerList = responseObject.getReviews();
			QuestionResponseReviewer questionReviewer = new QuestionResponseReviewer();
			questionReviewer.setId(UUID.randomUUID().toString());			
			questionReviewer.setComments(responseDTO.getComments());
			questionReviewer.setReviewerId(questionReviewer.getReviewerId());
			reviewerList.add(questionReviewer);			
			questionResponseReviewAuditService.updateQuestionAuditOnCreate(questionReviewer);
			questionResponseAuditService.updateQuestionAuditOnUpdate(responseObject);
			questionAuditService.updateQuestionAuditOnUpdate(dbQuestion);
			IndexResponse indexResponse = elasticsearchClient
					.index(i -> i.index(indexName).id(dbQuestion.getId()).document(dbQuestion));
			if (indexResponse.result().name().equals("Updated")) {
				
			} else {
				throw new Exception("Exception here");
			}	
			QuestionDTO dtoObject = questionMapper.toDto(dbQuestion);
			return dtoObject;
		} else {
			System.out.println("Yes there is no responses");
			
		}
		return null;
	}
	
	/**
	 * @param keyword
	 * @return
	 */
	public List<QuestionDTO> search(String keyword) throws Exception {
		Integer maxScore = 0;
		Boolean isPublic = true;
		Query scoreQuery = RangeQuery.of(r -> r 
				.field("score").gt(JsonData.of(maxScore)))._toQuery();	
		
		Query isPublicQuery = MatchQuery.of(m -> m 
				.field("isPublic").query(isPublic))._toQuery();
		
		Query searchQuery = MatchQuery.of(m -> m 
				.field("search_field")
				.query(keyword))._toQuery();
		
        SearchResponse<Question> response = elasticsearchClient.search(s -> s
            .index(indexName)
            .query(q -> q
	                .bool(b -> b 
		                    .must(searchQuery)
		                    .must(isPublicQuery)
		                    .must(scoreQuery)
		                )
		            ),
            Question.class
        );
		List<Hit<Question>> hits = response.hits().hits();
		List<Question> dbList = new ArrayList<>();
		for (Hit<Question> hit : hits) {
			Question question = hit.source();
			dbList.add(question);
		}
		List<QuestionDTO> dtoList = questionMapper.toDto(dbList);
		return dtoList;
	}
	
	public void deleteQuestion(String questionId) throws Exception {
		DeleteRequest request = DeleteRequest.of(d -> d.index(indexName).id(questionId));
		DeleteResponse deleteResponse = elasticsearchClient.delete(request);
		log.info(deleteResponse.result().toString());
	}
}
