package com.tech.truthapp.prayer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.tech.truthapp.model.prayer.Prayer;

@Repository
public interface PrayerRepository extends  MongoRepository<Prayer, String>{

	@Query("{ 'isApproved' : false, 'score' : {$gt : 0} }")
	public List<Prayer> findByReviewPrayers();
	
	@Query("{ 'createdBy' : ?0}")
	List<Prayer> findByPrayersByUserId(String userId);
	
	@Query("{ 'isPublic' : true, 'isApproved' : true }")
	public List<Prayer> findByReviewedPrayers();
	
	@Query("{ 'id' : ?0 , 'score' : {$gt : 0}}")
	public Optional<Prayer> findByPrayerForReview(String id);
	
}
