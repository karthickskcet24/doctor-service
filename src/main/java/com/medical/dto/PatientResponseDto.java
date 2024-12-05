package com.medical.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientResponseDto {
	
	private Long id;
	
	private List<GetConditionResponseDto> conditions;

}
