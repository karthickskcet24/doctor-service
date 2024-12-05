package com.medical.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CondtionResponseDto {
	private Long id;

	private String diagnosis;

	private String severity;

	private LocalDateTime dateOfDiagnosis;

	private String notes;

	private PractitionerResponseDto practitioner;
}
