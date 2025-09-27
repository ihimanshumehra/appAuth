package com.example.AuthApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.AuthApp.entities.RefreshToken;
import com.example.AuthApp.requests.AuthRequestDTO;
import com.example.AuthApp.requests.RefreshTokenRequestDTO;
import com.example.AuthApp.response.JwtResponseDTO;
import com.example.AuthApp.service.JwtService;
import com.example.AuthApp.service.RefreshTokenService;

import lombok.AllArgsConstructor;


@AllArgsConstructor
@Controller
public class TokenController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private RefreshTokenService refreshTokenService;
	
	@Autowired
	private JwtService jwtService;
	
	
	@PostMapping("auth/v1/login")
	public ResponseEntity AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO){
		System.out.println("i am in login");
		Authentication authentication = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(authRequestDTO.getUserName(), authRequestDTO.getPassword()));
		if (authentication.isAuthenticated()) {
			RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getUserName());
			
			String jwtToken = jwtService.GenerateToken(authRequestDTO.getUserName());
			
			return new ResponseEntity<>(JwtResponseDTO.builder().accessToken(jwtToken)
					.token(refreshToken.getToken()).build(), HttpStatus.OK
					);	
		}else {
			return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PostMapping("auth/v1/refreshToken")
	public JwtResponseDTO refreshToken (@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
		
		return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
				.map(refreshTokenService::verifyExpiration)
				.map(RefreshToken::getUserInfo)
				.map(userInfoDTO ->{
					String accessToken = jwtService.GenerateToken(userInfoDTO.getUsername());
					return JwtResponseDTO.builder()
							.accessToken(accessToken)
							.token(refreshTokenRequestDTO.getToken()).build();
				}).orElseThrow(() -> new RuntimeException("Refresh Token is not in DB..."));
	}
}
