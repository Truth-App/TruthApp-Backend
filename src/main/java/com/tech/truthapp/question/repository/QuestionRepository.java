package com.tech.truthapp.question.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.tech.truthapp.model.Question;

@Repository
public interface QuestionRepository extends  MongoRepository<Question, String>{

	
	public List<Question> findAll();
	
	@Query("{ 'createdBy' : ?0}")
	List<Question> findQuestionByUserId(String userId);
	
	@Query("{ 'isPublic' : true, 'isApproved' : true }")
	public List<Question> findByReviewedQuestions();
	
	@Query("{ 'isApproved' : false }")
	public List<Question> findByReviewQuestions();
	
	@Query("{ 'id' : ?0, 'score' : {$gt : ?1}}")
	public Optional<Question> findByIdAndScoreGreaterThan(String id, BigDecimal value);
	
	@Query("{ 'id' : ?0 }")
	public List<Question> findByIdInQuestions(List<String> ids);
	
	@Query("{ 'id' : ?0 }")
	public Optional<Question> findByIdAndScoreGreaterThan(String id);
	
	@Query("{ 'id' : ?0, 'score' : 2 }")
	public Optional<Question> findByIdAndScoreGreaterThanOne(String id);
	
}
