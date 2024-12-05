package com.medical.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

	@NotBlank(message = "Username is required!!!")
	private String username;

	@NotBlank(message = "Password is required!!!")
	@Pattern(regexp = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", message = "Enter a valid password!!!")
	private String password;

	@Pattern(regexp = "^(MALE|FEMALE|OTHERS)$", message = "Gender is must be male,female or other!!!")
	private String gender;

	@Pattern(regexp = "^(PATIENT|DOCTOR|PRACTITIONER)$", message = "Role must be folllowing one of this PATIENT,DOCTOR,PRACTITIONER!!!")
	@NotBlank(message = "Role is required!!!")
	private String role;

	//patiententityDto
	@NotBlank(message = "FirstName is required!!!")
	private String firstName;

	@NotBlank(message = "LastName is required!!!")
	private String lastName;

	@NotNull(message = "Date of birth is required!!!")
	@Past(message = "Invalid Date please give a proper Date!!!")
	private LocalDate dateOfBirth;

	@NotBlank(message = "Address is required!!!")
	@Size(min = 5,max = 500,message = "Address is must be 5 to 500 characters!!!")
	private String address;

	@NotBlank(message = "Contact is required!!!")
	@Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$",message = "Enter a valid phone number")
	private String contactInfo;

	//Practitioner entity
	 private String specialty;
	
	private Long organizationId;
	
}
