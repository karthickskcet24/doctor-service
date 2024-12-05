package com.medical.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationResponseDto {
	private Long id;

	private String name;

	private String address;

	private String department;

	//private OrganizationDTO organization;
}
