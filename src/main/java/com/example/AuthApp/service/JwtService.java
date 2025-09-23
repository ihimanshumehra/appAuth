package com.example.AuthApp.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	
	// 256 bits randomly generated cryptographic key (this should be stored in Key manager or other place)
	private static final String SECRET_KEY= "d6a8f3e42c1b9a7f0e8c3b25a4d76c2f3e9a1f5d0b7c8e2a4f3d6c1b9e8f7a2";

	
	public String extractUsername (String token) {
		return extractClaim(token, Claims::getSubject); 
		// claims -> claims.getSubject() --> getSubject returns("jwt.sub") which usually contains returns username of userId
		// e.g. for Email --> extractClaim(token, claims -> claims.get("email", String.class));
	}

	
	public Date extractExpiration (String token) {
		return extractClaim(token, Claims::getExpiration); 
	}
	
	public Boolean isTokenExpired (String token) {
		return extractExpiration(token).before(new Date()); 
	}
	
	public Boolean validateToken (String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equalsIgnoreCase(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		// private <T> means --> This declares a type placeholder named T. Java knows you’re using a "generic method"
		// T means --> This is the return type, and it will be whatever type the caller needs — like String, Date, etc.
		// Function<Claims, T>  --> piece of code (a function) that takes a "Claims object" and "returns a value of type T"
		final Claims claims = extractAllClaims(token);
		
		// Here -->  claimResolver  = Claims::getSubject  -- which is shorthand for --> claims -> claims.getSubject()
		return claimResolver.apply(claims);

	}
	
	@SuppressWarnings("deprecation")
	private Claims extractAllClaims(String token) { // decodes the token
		
		return Jwts
				.parser()
				.setSigningKey(getSignKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	@SuppressWarnings("deprecation")
	private String createToken (Map <String, Object> claims, String username) {
		
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 1))
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}
	
	

	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}


	public String GenerateToken(String name) {
		Map <String, Object> claims = new HashMap<>();
		return createToken(claims, name);
	}
	
}
