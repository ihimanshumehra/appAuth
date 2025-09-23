package com.example.AuthApp.auth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.AuthApp.service.JwtService;
import com.example.AuthApp.service.UserDetailsImplementService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Component
@AllArgsConstructor
@Data
public class JwtAuthFilter extends OncePerRequestFilter{
	
	@Autowired
	private final JwtService jwtService;
	
	@Autowired
	private final UserDetailsImplementService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		// 1. Read the "Authorization" header from the request
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String userName = null;
		
		// 2. Check if the header is present and starts with "Bearer "
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
	        // 3. Extract the actual token (skip "Bearer ")
	        token = authHeader.substring(7);

	        // 4. Extract the username from the token
	        userName = jwtService.extractUsername(token);
	    }
		
		
		if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
		    
		    // 1. Load user details from DB using username
		    UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
		    
		    // 2. Validate token against the user details
		    if (jwtService.validateToken(token, userDetails)) {
		        
		        // 3. Create an authentication token for Spring Security
		        UsernamePasswordAuthenticationToken authenticationToken =
		                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		        
		        // 4. Attach request details (IP, session info, etc.)
		        authenticationToken.setDetails(
		                new WebAuthenticationDetailsSource().buildDetails(request)
		        );
		        
		        // 5. Store authentication in SecurityContext (mark user as logged in)
		        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		    }
		}
		// 6. Continue request 
		filterChain.doFilter(request, response);
		// this fill move to the next filter in filterChain, 
		// if we have no further filter, it will pass request to the Controller.
	}
	
	
}
