package com.medical.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.medical.dto.EncounterDto;
import com.medical.dto.EncounterResponseDto;
import com.medical.dto.EncounterUpdateDto;
import com.medical.dto.PractitionerResponseDto;
import com.medical.dto.UserResponseDto;
import com.medical.entity.Encounter;
import com.medical.entity.Patient;
import com.medical.entity.Practitioner;
import com.medical.service.EncounterService;

@ExtendWith(MockitoExtension.class)
class EncounterControllerTest {

	@Mock
	private EncounterService encounterService;

	@InjectMocks
	private EncounterController encounterController;

	private EncounterDto encounterDto;
	private EncounterUpdateDto encounterUpdateDto;
	private Encounter encounter;
	private EncounterResponseDto encounterResponseDto;

	@BeforeEach
	void setUp() {
		encounterDto = new EncounterDto();
		encounterDto.setReasonForVisit("Routine Checkup");
		encounterDto.setPatientId(1L);
		encounterDto.setPractitionerId(1L);

		encounterUpdateDto = new EncounterUpdateDto();
		encounterUpdateDto.setReasonForVisit("Updated Checkup");
		encounterUpdateDto.setPractitionerId(1L);

		encounter = new Encounter();
		encounter.setId(1L);
		encounter.setReasonForVisit("Routine Checkup");

		Patient patient = new Patient();
		patient.setId(1L);

		Practitioner practitioner = new Practitioner();
		practitioner.setId(1L);

		encounter.setPatient(patient);
		encounter.setPractitioner(practitioner);

		encounterResponseDto = new EncounterResponseDto();
		encounterResponseDto.setReasonForVisit("Routine Checkup");

		UserResponseDto patientResponse = new UserResponseDto();
		patientResponse.setId(1L);
		;
		PractitionerResponseDto practitionerResponseDto = new PractitionerResponseDto();
		practitionerResponseDto.setId(1L);
		encounterResponseDto.setPatient(patientResponse);
		encounterResponseDto.setPractitioner(practitionerResponseDto);
	}

	@Test
	void createEncounter_ShouldReturnCreatedStatus() {
		when(encounterService.createEncounter(encounterDto)).thenReturn(encounter);

		ResponseEntity<Encounter> response = encounterController.createEncounter(encounterDto);

		assertAll(() -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
				() -> assertEquals(encounter, response.getBody()),
				() -> verify(encounterService, times(1)).createEncounter(encounterDto));
	}

	@Test
	void createEncounter_ShouldThrowRuntimeException_WhenServiceFails() {
		when(encounterService.createEncounter(encounterDto)).thenThrow(new RuntimeException("Service failed"));

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> encounterController.createEncounter(encounterDto));

		assertEquals("Service failed", exception.getMessage());
	}

	@Test
	void getEncounter_ShouldReturnOkStatus() {
		when(encounterService.getEncounter(1L)).thenReturn(encounterResponseDto);

		ResponseEntity<EncounterResponseDto> response = encounterController.getEncounter(1L);

		assertAll(() -> assertEquals(HttpStatus.OK, response.getStatusCode()),
				() -> assertEquals(encounterResponseDto, response.getBody()),
				() -> verify(encounterService, times(1)).getEncounter(1L));
	}

	@Test
	void getEncounter_ShouldThrowNotFoundException_WhenEncounterNotFound() {
		when(encounterService.getEncounter(1L)).thenThrow(new RuntimeException("Encounter not found"));

		RuntimeException exception = assertThrows(RuntimeException.class, () -> encounterController.getEncounter(1L));

		assertEquals("Encounter not found", exception.getMessage());
	}

	@Test
	void getEncounter_ShouldThrowRuntimeException_WhenServiceFails() {
		when(encounterService.getEncounter(1L)).thenThrow(new RuntimeException("Service failed"));

		RuntimeException exception = assertThrows(RuntimeException.class, () -> encounterController.getEncounter(1L));

		assertEquals("Service failed", exception.getMessage());
	}

	@Test
	void getEncounter_ShouldReturnOkStatus_WhenEncounterExists() {
		when(encounterService.getEncounter(1L)).thenReturn(encounterResponseDto);

		ResponseEntity<EncounterResponseDto> response = encounterController.getEncounter(1L);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void getAllEncounter_ShouldReturnOkStatus() {
		List<EncounterResponseDto> encounters = Arrays.asList(encounterResponseDto);
		when(encounterService.getAllEncounters(1L)).thenReturn(encounters);

		ResponseEntity<List<EncounterResponseDto>> response = encounterController.getAllEncounter(1L);

		assertAll(() -> assertEquals(HttpStatus.OK, response.getStatusCode()),
				() -> assertEquals(encounters, response.getBody()),
				() -> verify(encounterService, times(1)).getAllEncounters(1L));
	}

	@Test
	void getAllEncounter_ShouldReturnOkStatus_WhenEncountersExist() {
		List<EncounterResponseDto> encounters = Arrays.asList(encounterResponseDto);
		when(encounterService.getAllEncounters(1L)).thenReturn(encounters);

		ResponseEntity<List<EncounterResponseDto>> response = encounterController.getAllEncounter(1L);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void updateEncounter_ShouldReturnAcceptedStatus() {
		when(encounterService.updateEncounter(1L, encounterUpdateDto)).thenReturn(encounter);

		ResponseEntity<Encounter> response = encounterController.updateEncounter(1L, encounterUpdateDto);

		assertAll(() -> assertEquals(HttpStatus.ACCEPTED, response.getStatusCode()),
				() -> assertEquals(encounter, response.getBody()),
				() -> verify(encounterService, times(1)).updateEncounter(1L, encounterUpdateDto));
	}

	@Test
	void updateEncounter_ShouldThrowRuntimeException_WhenServiceFails() {
		when(encounterService.updateEncounter(1L, encounterUpdateDto))
				.thenThrow(new RuntimeException("Service failed"));

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> encounterController.updateEncounter(1L, encounterUpdateDto));

		assertEquals("Service failed", exception.getMessage());
	}

	@Test
	void updateEncounter_ShouldThrowNotFoundException_WhenEncounterNotFound() {
		when(encounterService.updateEncounter(1L, encounterUpdateDto))
				.thenThrow(new RuntimeException("Encounter not found"));

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> encounterController.updateEncounter(1L, encounterUpdateDto));

		assertEquals("Encounter not found", exception.getMessage());
	}
}
