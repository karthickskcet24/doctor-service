package com.medical.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.medical.dto.EncounterDto;
import com.medical.dto.EncounterUpdateDto;
import com.medical.entity.Encounter;
import com.medical.entity.Patient;
import com.medical.entity.Practitioner;
import com.medical.entity.User;
import com.medical.exception.AppException;
import com.medical.repository.EncounterRepository;
import com.medical.repository.PatientRepository;
import com.medical.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class EncounterServiceTest {

    @Mock
    private EncounterRepository encounterRepo;

    @Mock
    private PatientRepository patientRepo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private EncounterService encounterService;

    private EncounterDto encounterDto;
    private EncounterUpdateDto encounterUpdateDto;
    private Encounter encounter =new Encounter();
    private User practitioner = new User();
    private User patient = new User();

    @BeforeEach
    void setUp() {
        encounterDto = new EncounterDto();
        encounterDto.setPractitionerId(1L);
        encounterDto.setPatientId(2L);
        encounterDto.setReasonForVisit("Test Reason");

        encounterUpdateDto = new EncounterUpdateDto();
        encounterUpdateDto.setPractitionerId(1L);
        encounterUpdateDto.setReasonForVisit("Updated Reason");

        practitioner = new User();
        practitioner.setId(1L);
        practitioner.setRole("DOCTOR");

        patient = new User();
        patient.setId(2L);
        patient.setRole("PATIENT");

        encounter = new Encounter();
        encounter.setId(1L);
        
        Patient patient1 = new Patient();
        patient1.setId(1L);
        patient1.setUser(patient);
        
        Practitioner practitioner1 = new Practitioner();
        practitioner1.setId(2L);
        practitioner1.setUser(practitioner);
        
        encounter.setPatient(patient1);
        encounter.setPractitioner(practitioner1);
        encounter.setCreatedAt(LocalDateTime.now());
        encounter.setModifiedAt(LocalDateTime.now());
    }

    @Test
    void createEncounter_ShouldThrowNotFoundException_WhenPatientNotFound() {
        when(userRepo.findAllByIdIn(anyList())).thenReturn(List.of(practitioner));

        AppException exception = assertThrows(AppException.class, () -> encounterService.createEncounter(encounterDto));

        assertEquals("PatientId or PractitionerId not found", exception.getMessage());
    }

    @Test
    void createEncounter_ShouldThrowNotFoundException_WhenPractitionerNotFound() {
        when(userRepo.findAllByIdIn(anyList())).thenReturn(List.of(patient));

        AppException exception = assertThrows(AppException.class, () -> encounterService.createEncounter(encounterDto));

        assertEquals("PatientId or PractitionerId not found", exception.getMessage());
    }

    @Test
    void createEncounter_ShouldThrowNotFoundException_WhenInvalidRole() {
        practitioner.setRole("PATIENT"); // Invalid role for practitioner
        when(userRepo.findAllByIdIn(anyList())).thenReturn(List.of(practitioner, patient));

        AppException exception = assertThrows(AppException.class, () -> encounterService.createEncounter(encounterDto));

        assertEquals("PractitionerId not found", exception.getMessage());
    }

    @Test
    void createEncounter_ShouldThrowAppException_WhenGeneralErrorOccurs() {
        when(userRepo.findAllByIdIn(anyList())).thenThrow(new RuntimeException("Unexpected error"));

        AppException exception = assertThrows(AppException.class, () -> encounterService.createEncounter(encounterDto));

        assertTrue(exception.getMessage().contains("Unexpected error"));
    }

    @Test
    void updateEncounter_ShouldReturnUpdatedEncounter_WhenValidData() {
        when(encounterRepo.findById(1L)).thenReturn(Optional.of(encounter));
        when(userRepo.findById(encounterUpdateDto.getPractitionerId())).thenReturn(Optional.of(practitioner));
        when(encounterRepo.save(any(Encounter.class))).thenReturn(encounter);

        Encounter updatedEncounter = encounterService.updateEncounter(1L, encounterUpdateDto);

        assertNotNull(updatedEncounter);
        assertEquals("Updated Reason", updatedEncounter.getReasonForVisit());
        verify(encounterRepo).save(any(Encounter.class));
    }

    @Test
    void getEncounter_ShouldThrowNotFoundException_WhenEncounterNotFound() {
        when(encounterRepo.findById(1L)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> encounterService.getEncounter(1L));

        assertEquals("EncounterId not found", exception.getMessage());
    }

    @Test
    void getEncounter_ShouldThrowAppException_WhenGeneralErrorOccurs() {
        when(encounterRepo.findById(1L)).thenThrow(new RuntimeException("Unexpected error"));

        AppException exception = assertThrows(AppException.class, () -> encounterService.getEncounter(1L));

        assertTrue(exception.getMessage().contains("Unexpected error"));
    }

}
