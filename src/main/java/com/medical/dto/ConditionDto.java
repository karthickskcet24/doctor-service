package com.medical.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConditionDto {
	
	@NotBlank(message = "Diagnosis is required!!!")
	@Size(max =500 ,message = "Diagnosis is only contain 500 characters!!!")
	private String diagnosis;

	@NotBlank(message = "Severity is required!!!")
	@Size(max =500,message = "Diagnosis is only contain 500 characters!!!")
	private String severity;

	@NotBlank(message = "Notes is required!!!")
	@Size(min=5, max = 1000,message = "You can give between 5 to 1000 characters!!!")
	private String notes;
	
	@NotNull(message = "PractitionerId is required!!!")
	private long practitionerId;
	
	@NotNull(message = "EncounterId is required!!!")
	private long encounterId;
	
	
	
}
