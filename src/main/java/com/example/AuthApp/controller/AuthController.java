package com.example.AuthApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.AuthApp.entities.RefreshToken;
import com.example.AuthApp.model.UserInfoDto;
import com.example.AuthApp.response.JwtResponseDTO;
import com.example.AuthApp.service.JwtService;
import com.example.AuthApp.service.RefreshTokenService;
import com.example.AuthApp.service.UserDetailsImplementService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class AuthController {

	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private RefreshTokenService refreshTokenService;
	
	@Autowired
	private UserDetailsImplementService userDetailsImplementService;
	
	@PostMapping("auth/v1/signup")
	public ResponseEntity SignUp(@RequestBody UserInfoDto userInfoDto) {
		
		try {
			Boolean isSignUp = userDetailsImplementService.signupUser(userInfoDto);
			if (Boolean.FALSE.equals(isSignUp)) {
				return new ResponseEntity<>("Already Exists :",HttpStatus.BAD_REQUEST);
			}
			RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfoDto.getUsername());
			String jwtToken = jwtService.GenerateToken(userInfoDto.getUsername());
			
			return new ResponseEntity<>(JwtResponseDTO.builder().accessToken(jwtToken)
					.token(refreshToken.getToken()).build(), HttpStatus.OK
					);
			
		}catch (Exception e) {
			return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
