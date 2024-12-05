package com.medical.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {

	private Long id;

	private String name;
	
	private LocalDate dateOfBirth;

	private String address;

	private String contactInfo;
	
	private String username;

	private String role;
	
	private String gender;

	private LocalDateTime createdAt;
	
}
