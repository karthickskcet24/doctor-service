package com.medical.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
	private Long id;
	
	private Long userId;

	private String firstName;

	private String lastName;
	
	private LocalDate dateOfBirth;

	private String address;

	private String contactInfo;
	
	private String username;

	private String role;
	
	private List<GetConditionResponseDto> conditions;

	private String gender;
}
