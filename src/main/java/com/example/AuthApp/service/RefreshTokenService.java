package com.example.AuthApp.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.AuthApp.entities.RefreshToken;
import com.example.AuthApp.entities.UserInfo;
import com.example.AuthApp.repository.RefreshTokenRepository;
import com.example.AuthApp.repository.UserRepository;


@Service
public class RefreshTokenService {

	@Autowired
	RefreshTokenRepository refreshTokenRepository;
	
	@Autowired
	UserRepository userRepository;
	
	public RefreshToken createRefreshToken(String userName) {
	    // Fetch the UserInfo entity from the database using the username
	    UserInfo userInfoExtract = userRepository.findByUsername(userName);

	    // Check if a refresh token already exists for this user
	    Optional<RefreshToken> existingTokenOpt = refreshTokenRepository.findByUserInfo(userInfoExtract);

	    if (existingTokenOpt.isPresent()) {
	        // If a refresh token exists, retrieve it
	        RefreshToken existingToken = existingTokenOpt.get();

	        // Generate a new random token string
	        existingToken.setToken(UUID.randomUUID().toString());

	        // Set a new expiry date 10 minutes (600000 milliseconds) from now
	        existingToken.setExpriry_date(Instant.now().plusMillis(600000));

	        // Save and return the updated refresh token entity
	        return refreshTokenRepository.save(existingToken);
	    } else {
	        // No existing refresh token found for this user, so create a new one

	        // Build a new RefreshToken entity with user info, random token, and expiry date
	        RefreshToken refreshToken = RefreshToken.builder()
	            .userInfo(userInfoExtract) // Associate with user
	            .token(UUID.randomUUID().toString()) // Generate unique token string
	            .expriry_date(Instant.now().plusMillis(600000)) // Set expiry date 10 minutes ahead
	            .build();

	        // Save the new refresh token entity to the database and return it
	        return refreshTokenRepository.save(refreshToken);
	    }
	}

	
	public RefreshToken verifyExpiration (RefreshToken token) {
		if (token.getExpriry_date().compareTo(Instant.now()) <0) {
			refreshTokenRepository.delete(token); // .delete() -->  default method in CRUDRepository 
			throw new RuntimeException(token.getToken() + " Token has expired ");
		}
		return token;
	}

	public  Optional<RefreshToken> findByToken(String token) {
		
		return refreshTokenRepository.findByToken(token);
	}
	
	public Optional<RefreshToken> findByUserInfo(UserInfo userInfo) {
	    return refreshTokenRepository.findByUserInfo(userInfo);
	}
}
