package com.medical.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {

	@NotBlank(message = "Please give username")
	private String username;
	
	@NotBlank(message = "Please give password ")
	private String password; 
	
}
