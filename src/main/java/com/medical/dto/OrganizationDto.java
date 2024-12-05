package com.medical.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationDto {

	@NotBlank(message = "Name is  required")
	private String name;
	@NotBlank(message = "Address is  required")
	private String address;
	@NotBlank(message = "Phone is  required")
	@Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$", message = "Enter a valid phone number")
	private String phone;
}
