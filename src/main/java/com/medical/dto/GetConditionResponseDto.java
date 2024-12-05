package com.medical.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GetConditionResponseDto {
	
	private Long id;

	private String diagnosis;

	private String severity;

	private LocalDateTime dateOfDiagnosis;

	private String notes;
}
