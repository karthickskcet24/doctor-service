package com.medical.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medical.dto.EncounterDto;
import com.medical.dto.EncounterResponseDto;
import com.medical.dto.EncounterUpdateDto;
import com.medical.entity.Encounter;
import com.medical.service.EncounterService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/encounter")
@CrossOrigin("*")
public class EncounterController {

	@Autowired
	EncounterService encounteService;

	@PostMapping
	public ResponseEntity<Encounter> createEncounter(@Valid @RequestBody EncounterDto encounterDto) {

		Encounter organization = encounteService.createEncounter(encounterDto);
		return new ResponseEntity<Encounter>(organization, HttpStatus.CREATED);
	}

	@GetMapping("{encounterId}")
	public ResponseEntity<EncounterResponseDto> getEncounter(@PathVariable(name = "encounterId") long encounterId) {

		EncounterResponseDto organization = encounteService.getEncounter(encounterId);
		return new ResponseEntity<EncounterResponseDto>(organization, HttpStatus.OK);
	}

	@GetMapping("user/{userId}")
	public ResponseEntity<List<EncounterResponseDto>> getAllEncounter(@PathVariable(name = "userId") long userId) {

		List<EncounterResponseDto> organization = encounteService.getAllEncounters(userId);
		return new ResponseEntity<List<EncounterResponseDto>>(organization, HttpStatus.OK);
	}

	@PutMapping("{encounterId}")
	public ResponseEntity<Encounter> updateEncounter(@PathVariable(name = "encounterId") long encounterId,
			@Valid @RequestBody EncounterUpdateDto encounterDto) {

		Encounter organization = encounteService.updateEncounter(encounterId, encounterDto);
		return new ResponseEntity<Encounter>(organization, HttpStatus.ACCEPTED);
	}

}
