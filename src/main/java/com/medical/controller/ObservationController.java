package com.medical.controller;

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

import com.medical.dto.ObservationDto;
import com.medical.entity.Observation;
import com.medical.service.ObservationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/observation")
@CrossOrigin("*")
public class ObservationController {

	@Autowired
	ObservationService observationService;

	@PostMapping
	public ResponseEntity<Observation> createObservation(@Valid @RequestBody ObservationDto observationDto) {
		Observation observation = observationService.createObservation(observationDto);
		return new ResponseEntity<Observation>(observation, HttpStatus.CREATED);
	}

	@GetMapping("{observationId}")
	public ResponseEntity<Observation> getObservation(@PathVariable(name = "observationId") long observationId) {
		Observation observation = observationService.getObservation(observationId);
		return new ResponseEntity<Observation>(observation, HttpStatus.OK);
	}

	@PutMapping("{observationId}")
	public ResponseEntity<Observation> updateObservation(@PathVariable(name = "observationId") long observationId,
			@RequestBody ObservationDto observationDto) {
		Observation observation = observationService.updateObservation(observationId, observationDto);
		return new ResponseEntity<Observation>(observation, HttpStatus.ACCEPTED);
	}
}
