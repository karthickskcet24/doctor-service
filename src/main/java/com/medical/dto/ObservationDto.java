package com.medical.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObservationDto {

	@NotBlank(message = "Type is required")
	private String type;

	@NotBlank(message = "Type is required")
	private String value;

	@NotBlank(message = "Unit is required")
	private String unit;

	@NotBlank(message = "Notes is required")
	private String notes;

	@NotBlank(message = "Name is required")
	private String name;

	@NotBlank(message = "Address is required")
	private String address;

	@NotBlank(message = "Department is required")
	private String department;

	private long practitionerId;

	private long encounterId;

}
