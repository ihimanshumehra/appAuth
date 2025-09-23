package com.example.AuthApp.util;

import java.util.Objects;

public class ValidationUtil {

	// Helper for email validation (simple regex)
	public static boolean isEmailValid(String email) {
	    // Basic pattern for demonstration; use better patterns in production
	    String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
	    return email != null && email.matches(emailRegex);
	}

	//-----------------------------------------------------------------
	// Helper for password strength validation (example: min 8 chars)
	public static boolean isPasswordValid(String password) {
	    return password != null && password.length() >= 8;
	}
	
	//-----------------------------------------------------------------
}
