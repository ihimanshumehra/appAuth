package com.example.AuthApp.service;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.AuthApp.entities.UserInfo;
import com.example.AuthApp.model.UserInfoDto;
import com.example.AuthApp.repository.UserRepository;
import com.example.AuthApp.util.ValidationUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Data
public class UserDetailsImplementService implements UserDetailsService{

	
	@Autowired
	private final UserRepository userRepository;
	
	@Autowired
	private final PasswordEncoder passwordEncoder;

	// -----------------------------------------------------------------------
	@Override
	 // Load user by username required by Spring Security
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserInfo userInfo = userRepository.findByUsername(username);
		if (userInfo == null){
			throw new UsernameNotFoundException("could not find user...");
		}
		// Return a custom UserDetails object for authentication
		return new com.example.AuthApp.service.CustomUserDetails(userInfo);
	}
	
	// -----------------------------------------------------------------------
	 // Check if user already exists by username
	public UserInfo checkIfUserAlreadyExists(UserInfoDto userInfoDto) {
		
		return userRepository.findByUsername(userInfoDto.getUsername());
	}

	// -----------------------------------------------------------------------
	 // Signup logic with secure password encoding
	public boolean signupUser (UserInfoDto userInfoDto) {
		//Define a function to check if userEmail, password is correct
		
		if (Objects.nonNull(checkIfUserAlreadyExists(userInfoDto))) {
			return false ; // as user already exists , no need of signup
		}
	    // Optionally: Validate email and password format here
	    if (!ValidationUtil.isEmailValid(userInfoDto.getUsername()) || !ValidationUtil.isPasswordValid(userInfoDto.getPassword())) {
	        return false;
	    }
		
		// Encode password securely before saving
		String encodedPassword = passwordEncoder.encode(userInfoDto.getPassword());
		String userId = UUID.randomUUID().toString();
		
		userRepository.save(new UserInfo(userId, userInfoDto.getUsername(), encodedPassword, new HashSet<>()));
		
		return true;
	}
	
	
}
