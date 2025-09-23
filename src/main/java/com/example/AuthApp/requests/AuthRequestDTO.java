package com.example.AuthApp.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// @Data equivalent to @Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode. 
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequestDTO {

	private String userName;
	private String password;
}
