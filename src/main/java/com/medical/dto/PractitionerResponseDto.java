package com.medical.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PractitionerResponseDto {
	private Long id;

	private String specialty;

	private OrganizationResponseDto organization;
	
	private Long userId;

	private String firstName;

	private String lastName;
	
	private LocalDate dateOfBirth;

	private String address;

	private String contactInfo;
	
	private String username;

	private String role;
	
	private String gender;
}
