package com.example.AuthApp.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.AuthApp.entities.UserInfo;
import com.example.AuthApp.entities.UserRole;


public class CustomUserDetails implements UserDetails 
{
	// For Serialization class , we have to --> declare a static final serialVersionUID field of type long
	private static final long serialVersionUID = 1L;

	private String username;
	
	private String password;
	
	Collection<? extends GrantedAuthority> authorities;
	
	
	

	public CustomUserDetails(UserInfo userinfo) {
		super();
		this.username = userinfo.getUsername(); //get user name 
		this.password = userinfo.getPassword();
		
		// <GrantedAuthority> Represents an authority granted to an "Authentication" object. 
		List<GrantedAuthority> auths = new ArrayList<>(); // will make list for authorities based on roles
		
		for (UserRole role : userinfo.getRoles()) {
			auths.add(new SimpleGrantedAuthority(role.getRoleName().toUpperCase())); // adding authorities to Roles
		}
		
		this.authorities = auths;
	}
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}
	
	@Override
	public String getPassword() {
		return password;
		
	}
	
	// 🔑 These four are REQUIRED, otherwise login can fail
    @Override
    public boolean isAccountNonExpired() {
        return true; // add logic if you want expiry
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // add logic if you want lockout
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // add password expiry policy if needed
    }

    @Override
    public boolean isEnabled() {
        return true; // hook into UserInfo.active flag if you have one
    }
	
	
}
