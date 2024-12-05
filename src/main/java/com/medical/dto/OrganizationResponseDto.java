package com.medical.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationResponseDto {
	private Long id;

	private String name;

	private String address;

	private String phone;
}
