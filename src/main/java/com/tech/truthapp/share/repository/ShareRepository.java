package com.tech.truthapp.share.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.tech.truthapp.model.share.Share;

@Repository
public interface ShareRepository extends  MongoRepository<Share, String>{

	@Query("{ 'isApproved' : false, 'score' : {$gt : 0} }")
	public List<Share> findByReviewShare();
	
	@Query("{ 'createdBy' : ?0}")
	List<Share> findByShareByUserId(String userId);
	
	@Query("{ 'isPublic' : true, 'isApproved' : true }")
	public List<Share> findByReviewedShares();
	
	@Query("{ 'id' : ?0 , 'score' : {$gt : 0}}")
	public Optional<Share> findByShareForReview(String id);
	
}
