package com.medical.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.medical.constant.RoleType;
import com.medical.dto.ObservationDto;
import com.medical.entity.Encounter;
import com.medical.entity.Location;
import com.medical.entity.Observation;
import com.medical.entity.Patient;
import com.medical.entity.Practitioner;
import com.medical.entity.User;
import com.medical.exception.AppException;
import com.medical.repository.EncounterRepository;
import com.medical.repository.ObservationRepository;
import com.medical.repository.UserRepository;

class ObservationServiceTest {

	@Mock
	private ObservationRepository observationRepo;

	@Mock
	private EncounterRepository encounterRepo;

	@Mock
	private UserRepository userRepo;

	@InjectMocks
	private ObservationService observationService;

	private User practitioner;
	private User patient;
	private Encounter encounter;
	private ObservationDto observationDto;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		practitioner = new User();
		practitioner.setId(1L);
		practitioner.setRole(RoleType.PRACTITIONER.name());

		patient = new User();
		patient.setId(2L);
		patient.setRole(RoleType.PATIENT.name());

		encounter = new Encounter();
		encounter.setId(1L);

		Patient patient1 = new Patient();
		patient1.setId(1L);
		patient1.setUser(patient);
		encounter.setPatient(patient1);

		observationDto = new ObservationDto();
		observationDto.setPractitionerId(1L);
		observationDto.setEncounterId(1L);
		observationDto.setType("Blood Test");
		observationDto.setNotes("Normal");
		observationDto.setValue("120");
		observationDto.setUnit("mg/dL");
		observationDto.setName("Lab");
		observationDto.setDepartment("Cardiology");
		observationDto.setAddress("123 Health St.");
	}

	@Test
	void testCreateObservation_Success() {
		when(userRepo.findById(1L)).thenReturn(Optional.of(practitioner));
		when(encounterRepo.findById(1L)).thenReturn(Optional.of(encounter));
		when(observationRepo.save(any(Observation.class))).thenReturn(new Observation());

		Observation result = observationService.createObservation(observationDto);

		assertNotNull(result);
		verify(observationRepo, times(1)).save(any(Observation.class));
	}

	@Test
	void testCreateObservation_Fail_PatientRole() {
		observationDto.setPractitionerId(2L); 

		when(userRepo.findById(2L)).thenReturn(Optional.of(patient));

		Exception exception = assertThrows(AppException.class,
				() -> observationService.createObservation(observationDto));
		assertEquals("You don't have permission", exception.getMessage());
	}

	@Test
	void testCreateObservation_Fail_UserNotFound() {
		when(userRepo.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(AppException.class,
				() -> observationService.createObservation(observationDto));
		assertEquals("UserId not found", exception.getMessage());
	}

	@Test
	void testCreateObservation_Fail_EncounterNotFound() {
		when(userRepo.findById(1L)).thenReturn(Optional.of(practitioner));
		when(encounterRepo.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(AppException.class,
				() -> observationService.createObservation(observationDto));
		assertEquals("EncounterId not found", exception.getMessage());
	}

	@Test
	void testUpdateObservation_Success() {
	    Observation existingObservation = new Observation();
	    existingObservation.setId(1L);
	    existingObservation.setCreatedAt(LocalDateTime.now());
	    
	    Location location = new Location();
	    location.setId(1L);
	    location.setName("test");
	    existingObservation.setLocation(location);
	    
	    Practitioner practitioner = new Practitioner();
	    practitioner.setId(1L);
	    existingObservation.setPractitioner(practitioner);

	    User user = new User();
	    user.setId(1L);
	    user.setRole(RoleType.PRACTITIONER.name());

	    when(userRepo.findById(1L)).thenReturn(Optional.of(user));
	    when(observationRepo.findById(1L)).thenReturn(Optional.of(existingObservation));
	    when(observationRepo.save(any(Observation.class))).thenReturn(existingObservation);

	    Observation result = observationService.updateObservation(1L, observationDto);

	    assertNotNull(result);
	    assertEquals(observationDto.getType(), result.getType());
	    assertEquals(observationDto.getNotes(), result.getNotes());
	}


	@Test
	void testUpdateObservation_Fail_PatientRole() {
		observationDto.setPractitionerId(2L);

		when(userRepo.findById(2L)).thenReturn(Optional.of(patient));

		Exception exception = assertThrows(AppException.class,
				() -> observationService.updateObservation(1L, observationDto));
		assertEquals("You don't have permission", exception.getMessage());
	}

	@Test
	void testUpdateObservation_Fail_ObservationNotFound() {
		when(userRepo.findById(1L)).thenReturn(Optional.of(practitioner));
		when(observationRepo.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(AppException.class,
				() -> observationService.updateObservation(1L, observationDto));
		assertEquals("ObservationId not found", exception.getMessage());
	}

	@Test
	void testGetObservation_Success() {
		Observation observation = new Observation();
		observation.setId(1L);
		observation.setType("Blood Test");

		when(observationRepo.findById(1L)).thenReturn(Optional.of(observation));

		Observation result = observationService.getObservation(1L);

		assertNotNull(result);
		assertEquals("Blood Test", result.getType());
	}

	@Test
	void testGetObservation_Fail_NotFound() {
		when(observationRepo.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(AppException.class, () -> observationService.getObservation(1L));
		assertEquals("ObservationId not found", exception.getMessage());
	}
}
