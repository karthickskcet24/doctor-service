package com.medical.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EncounterDto {

	@NotBlank(message = "Reason for visit is required")
	private String reasonForVisit;

	private long patientId;
	
	private long practitionerId;
}
