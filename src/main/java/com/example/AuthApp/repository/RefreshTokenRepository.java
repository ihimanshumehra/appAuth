package com.example.AuthApp.repository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.example.AuthApp.entities.RefreshToken;

@Repository
public interface RefreshTokenRepository extends CrudRepository <RefreshToken, Integer>{
	
	Optional<RefreshToken> findByToken(String token); // SprintBoot uses RefreshToken entity and automatically make queries to DB to provide RefreshToken 
	
	
}
