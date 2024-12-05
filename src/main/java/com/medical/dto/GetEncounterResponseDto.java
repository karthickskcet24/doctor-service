package com.medical.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetEncounterResponseDto {
	
	private Long id;

	private LocalDateTime createdAt;

	private LocalDateTime modifiedAt;

	private String reasonForVisit;

	private UserResponseDto patient;

	private PractitionerResponseDto practitioner;

	private List<ObservationResponseDto> observations;

	private List<CondtionResponseDto> condition;
}
