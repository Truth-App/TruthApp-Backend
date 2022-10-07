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
	
	@Query("{ 'isPublic' : true, 'isApproved' : true, 'category' : ?0 }")
	public List<Question> findByReviewedQuestionsForCategory(String category);
	
	@Query("{ 'isApproved' : false, 'score' : {$gt : 0} }")
	public List<Question> findByReviewQuestions();
	
	//'responses' : { $in : ['isApproved' : false]} }
	@Query("{ 'responses.isApproved' : false, 'responses.score' : {$gt : 0} } } ")
	public List<Question> findByReviewResponses();
	
	@Query("{ 'id' : ?0 }")
	public List<Question> findByIdInQuestions(List<String> ids);
	
	@Query("{ 'id' : ?0 , 'score' : {$gt : 0}}")
	public Optional<Question> findByquestionForReview(String id);
	
	@Query("{ 'id' : ?0, 'score' : 2 }")
	public Optional<Question> findByIdAndScoreGreaterThanOne(String id);
	
	public List<Question> findByIsApprovedIsFalseAndScoreGreaterThan(BigDecimal value);
	
	public List<Question> findByIsApprovedIsFalse();
	
	public List<Question> findByScoreGreaterThan(BigDecimal value);
	
	@Query("{ 'isPublic' : true, 'isApproved' : true, 'responses.0' { $exists : true } }")
	public List<Question> findByMyReviewedQuestions();
}
