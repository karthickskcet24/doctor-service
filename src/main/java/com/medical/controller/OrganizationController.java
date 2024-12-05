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

import com.medical.dto.OrganizationDto;
import com.medical.dto.OrganizationResponseDto;
import com.medical.entity.Organization;
import com.medical.service.OrganizationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/organization")
@CrossOrigin("*")
public class OrganizationController {

	@Autowired
	OrganizationService organizationService;

	@PostMapping
	public ResponseEntity<Organization> createOrganization(@Valid @RequestBody OrganizationDto organizationDto) {

		Organization organization = organizationService.createOrganization(organizationDto);
		return new ResponseEntity<Organization>(organization, HttpStatus.CREATED);
	}

	
	@GetMapping("{organizationId}")
	public ResponseEntity<OrganizationResponseDto> getOrganization(@PathVariable(name = "organizationId") long organizationId) {

		OrganizationResponseDto organization = organizationService.getOrganization(organizationId);
		return new ResponseEntity<OrganizationResponseDto>(organization, HttpStatus.OK);
	}
	@GetMapping
	public ResponseEntity<List<OrganizationResponseDto>> getAllOrganization() {

		List<OrganizationResponseDto> organizations = organizationService.getAllOrganizations();
		return new ResponseEntity<List<OrganizationResponseDto>> (organizations, HttpStatus.OK);
	}


	@PutMapping("{organizationId}")
	public ResponseEntity<Organization> updateOrganization(@PathVariable long organizationId,
			 @RequestBody OrganizationDto organizationDto) {

		Organization organization = organizationService.updateOrganization(organizationId, organizationDto);
		return new ResponseEntity<Organization>(organization, HttpStatus.ACCEPTED);
	}

}
