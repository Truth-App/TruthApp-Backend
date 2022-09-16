package com.tech.truthapp.question.repository;

import java.util.List;

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
	
	@Query("{ 'id' : ?0 }")
	public List<Question> findByIdInQuestions(List<String> ids);
}
