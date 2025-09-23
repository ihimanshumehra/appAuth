package com.example.AuthApp.model;

import com.example.AuthApp.entities.UserInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserInfoDto extends UserInfo{

	private String userName; // in Snake case strategy --> user_name
	
	private String lastName;
	
	private Long phone;
	
	private String email;
}
