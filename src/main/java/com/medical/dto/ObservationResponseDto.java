package com.medical.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObservationResponseDto {
	private Long id;

	private String type;

	private String value;

	private String unit;

	private LocalDateTime createdAt;

	private String notes;

	private LocationResponseDto location;

	private PractitionerResponseDto practitioner;
}
