package com.medical.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDto {
	
	private long userId;
	
	private String firstName;
	
	private String LastName;
	
	private String address;
	
	private String contactInfo;
	
	@Past
	private LocalDate getDateOfBirth;
}
