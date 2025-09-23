package com.example.AuthApp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.AuthApp.entities.UserInfo;

@Repository
public interface UserRepository extends CrudRepository<UserInfo, String>{
	
	
	public UserInfo findByUsername (String username);
	// SprintBoot uses UserInfo entity and automatically make queries to DB to provide UserInfo object.
	
	UserInfo findByUsernameAndPassword (String username, String password); 
	// SprintBoot uses UserInfo entity and automatically make queries to DB to provide UserInfo object.
	
}
