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

import com.medical.dto.ConditionDto;
import com.medical.dto.ConditionUpdateDto;
import com.medical.entity.Condition;
import com.medical.service.ConditionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/condition")
@CrossOrigin("*")
public class ConditionController {

	@Autowired
	ConditionService conditionService;

	@PostMapping
	public ResponseEntity<Condition> createCondition(@Valid @RequestBody ConditionDto conditionDto) {
		Condition createCondition = conditionService.createCondition(conditionDto);
		return new ResponseEntity<>(createCondition, HttpStatus.CREATED);
	}

	@PutMapping("/{conditionId}")
	public ResponseEntity<Condition> updateCondition(@PathVariable("conditionId") long conditionId,
			@RequestBody ConditionUpdateDto conditionDto) {
		Condition updateCondition = conditionService.updateCondition(conditionId, conditionDto);
		return new ResponseEntity<>(updateCondition, HttpStatus.ACCEPTED);

	}

	@GetMapping("/{conditionId}")
	public ResponseEntity<Condition> getCondition(@PathVariable("conditionId") long conditionId) {
		Condition getCondition = conditionService.getConditon(conditionId);

		return new ResponseEntity<>(getCondition, HttpStatus.OK);
	}

}
