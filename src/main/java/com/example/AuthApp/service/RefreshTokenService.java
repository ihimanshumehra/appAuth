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
	
	public RefreshToken createRefreshToken (String userName) {
		UserInfo userInfoExtract = userRepository.findByUsername(userName);
		
		// now we will create refresh token as per our Refresh Token entity
		RefreshToken refreshToken = RefreshToken.builder()
												.userInfo(userInfoExtract)
												.token(UUID.randomUUID().toString())
												.expriry_date(Instant.now().plusMillis(600000))
												.build();
		
		return refreshTokenRepository.save(refreshToken); // .save is a JPA method
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
	
}
