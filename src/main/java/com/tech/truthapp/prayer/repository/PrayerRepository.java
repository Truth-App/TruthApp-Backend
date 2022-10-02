package com.tech.truthapp.prayer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tech.truthapp.model.Prayer;

@Repository
public interface PrayerRepository extends  MongoRepository<Prayer, String>{

	
	
}
