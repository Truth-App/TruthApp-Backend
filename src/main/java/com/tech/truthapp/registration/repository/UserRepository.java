package com.tech.truthapp.registration.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.tech.truthapp.model.User;

@Repository
public interface UserRepository extends  MongoRepository<User, String>{

	@Query("{ 'userId' : ?0, 'userActive' : true}")
	public List<User> findByActiveUserId(String userId);
	
	List<User> findByUserId(String userId);
}
