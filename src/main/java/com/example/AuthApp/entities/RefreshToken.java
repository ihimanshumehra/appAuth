package com.example.AuthApp.entities;

import java.time.Instant;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming (PropertyNamingStrategies.SnakeCaseStrategy.class)
@Table(name = "tokens")
public class RefreshToken {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long id;
	
	private String token;
	
	private Instant expriry_date;
	
	@OneToOne
	@JoinColumn (name = "user_id" , referencedColumnName = "user_id")
	private UserInfo userInfo;
}
