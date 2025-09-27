package com.example.AuthApp.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.AuthApp.repository.UserRepository;
import com.example.AuthApp.service.UserDetailsImplementService;

import jakarta.annotation.security.PermitAll;
import lombok.Data;

@Configuration
@EnableMethodSecurity
@Data
public class SecurityConfig {

	@Autowired
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	private final UserDetailsImplementService userDetailsImpl;
	
	// -------------------------------------------------------------------
	public UserDetailsService userDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		
		return new UserDetailsImplementService(userRepository, passwordEncoder)	;
	}
	
	// -------------------------------------------------------------------
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtAuthFilter jwtAuthFilter) throws Exception {
		
		httpSecurity
	      // Apply this chain only to API routes if desired
	      // .securityMatcher(antMatcher("/api/**"))
	      .cors(cors -> cors.disable())                    // Prefer global CORS via WebMvcConfigurer
	      .csrf(csrf -> csrf.disable())                    // JWT + stateless => CSRF off
	      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	      .authorizeHttpRequests(auth -> auth
	    		// API end points
	          .requestMatchers(
	              "/auth/v1/login",
	              "/auth/v1/refreshToken",
	              "/auth/v1/signup"
	          ).permitAll()
	          // Static resources - ADD THESE LINES
	            .requestMatchers(
	                "/",
	                "/login.html",
	                "/index.html",
	                "/**/*.html",
	                "/**/*.css",
	                "/**/*.js",
	                "/**/*.png",
	                "/**/*.jpg",
	                "/**/*.ico"
	            ).permitAll()
	          .anyRequest().authenticated()
	      )
	      .authenticationProvider(authenticationProvider())
	      .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
	      //.httpBasic(Customizer.withDefaults());           // Optional for quick testing

	    return httpSecurity.build();
	}
	
	// -------------------------------------------------------------------
	
	@Bean
	public AuthenticationProvider authenticationProvider() {

	    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	    provider.setUserDetailsService(userDetailsImpl);
	    provider.setPasswordEncoder(passwordEncoder); // e.g., new BCryptPasswordEncoder()
	    return provider;
	}
	
	// -------------------------------------------------------------------
	@Bean
	public AuthenticationManager authenticationManager (AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
}
