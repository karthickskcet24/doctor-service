package com.medical.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.medical.dto.ConditionDto;
import com.medical.dto.ConditionUpdateDto;
import com.medical.entity.Condition;
import com.medical.entity.Encounter;
import com.medical.entity.Patient;
import com.medical.entity.User;
import com.medical.exception.AppException;
import com.medical.repository.ConditionRepository;
import com.medical.repository.EncounterRepository;
import com.medical.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class ConditionServiceTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private ConditionRepository conditionRepo;

    @Mock
    private EncounterRepository encounterRepo;

    @InjectMocks
    private ConditionService conditionService;

    private ConditionDto conditionDto;
    private ConditionUpdateDto conditionUpdateDto;
    private User practitioner;
    private Encounter encounter;

    @BeforeEach
    void setUp() {
        conditionDto = new ConditionDto();
        conditionDto.setEncounterId(1L);
        conditionDto.setPractitionerId(2L);
        conditionDto.setNotes("Condition notes");
        conditionDto.setDiagnosis("Condition Diagnosis");
        conditionDto.setSeverity("High");

        conditionUpdateDto = new ConditionUpdateDto();
        conditionUpdateDto.setPractitionerId(2L);
        conditionUpdateDto.setNotes("Updated notes");
        conditionUpdateDto.setDiagnosis("Updated Diagnosis");
        conditionUpdateDto.setSeverity("Low");

        practitioner = new User();
        practitioner.setId(2L);
        practitioner.setRole("DOCTOR");

        encounter = new Encounter();
        encounter.setId(1L);
        encounter.setPatient(new Patient());
    }

    @Test
    void createCondition_ShouldReturnCondition_WhenValidData() {
        when(encounterRepo.findById(conditionDto.getEncounterId())).thenReturn(Optional.of(encounter));
        when(userRepo.findById(conditionDto.getPractitionerId())).thenReturn(Optional.of(practitioner));
        when(conditionRepo.save(any(Condition.class))).thenReturn(new Condition());

        Condition condition = conditionService.createCondition(conditionDto);

        assertNotNull(condition);
        verify(encounterRepo).findById(conditionDto.getEncounterId());
        verify(userRepo).findById(conditionDto.getPractitionerId());
        verify(conditionRepo).save(any(Condition.class));
    }

    @Test
    void createCondition_ShouldThrowAppException_WhenUserIsPatient() {
        practitioner.setRole("PATIENT");
        when(encounterRepo.findById(conditionDto.getEncounterId())).thenReturn(Optional.of(encounter));
        when(userRepo.findById(conditionDto.getPractitionerId())).thenReturn(Optional.of(practitioner));

        AppException exception = assertThrows(AppException.class, () -> conditionService.createCondition(conditionDto));

        assertEquals("You don't have permission", exception.getMessage());
    }

    @Test
    void createCondition_ShouldThrowAppException_WhenGeneralErrorOccurs() {
        when(encounterRepo.findById(conditionDto.getEncounterId())).thenThrow(new RuntimeException("Unexpected error"));

        AppException exception = assertThrows(AppException.class, () -> conditionService.createCondition(conditionDto));

        assertTrue(exception.getMessage().contains("Unexpected error"));
    }

    @Test
    void updateCondition_ShouldReturnUpdatedCondition_WhenValidData() {
        Condition existingCondition = new Condition();
        existingCondition.setId(1L);
        existingCondition.setNotes("Old notes");
        when(conditionRepo.findById(1L)).thenReturn(Optional.of(existingCondition));
        when(userRepo.findById(conditionUpdateDto.getPractitionerId())).thenReturn(Optional.of(practitioner));
        when(conditionRepo.save(any(Condition.class))).thenReturn(existingCondition);

        Condition updatedCondition = conditionService.updateCondition(1L, conditionUpdateDto);

        assertNotNull(updatedCondition);
        assertEquals("Updated notes", updatedCondition.getNotes());
        verify(conditionRepo).findById(1L);
        verify(userRepo).findById(conditionUpdateDto.getPractitionerId());
    }

    @Test
    void updateCondition_ShouldThrowAppException_WhenUserIsPatient() {
        practitioner.setRole("PATIENT");
        when(conditionRepo.findById(1L)).thenReturn(Optional.of(new Condition()));
        when(userRepo.findById(conditionUpdateDto.getPractitionerId())).thenReturn(Optional.of(practitioner));

        AppException exception = assertThrows(AppException.class, () -> conditionService.updateCondition(1L, conditionUpdateDto));

        assertEquals("You dont have permission", exception.getMessage());
    }

    @Test
    void updateCondition_ShouldThrowAppException_WhenGeneralErrorOccurs() {
        when(conditionRepo.findById(1L)).thenReturn(Optional.of(new Condition()));
        when(userRepo.findById(conditionUpdateDto.getPractitionerId())).thenThrow(new RuntimeException("Unexpected error"));

        AppException exception = assertThrows(AppException.class, () -> conditionService.updateCondition(1L, conditionUpdateDto));

        assertTrue(exception.getMessage().contains("Unexpected error"));
    }

    @Test
    void getCondition_ShouldReturnCondition_WhenConditionExists() {
        Condition condition = new Condition();
        condition.setId(1L);
        when(conditionRepo.findById(1L)).thenReturn(Optional.of(condition));

        Condition retrievedCondition = conditionService.getConditon(1L);

        assertNotNull(retrievedCondition);
        assertEquals(1L, retrievedCondition.getId());
        verify(conditionRepo).findById(1L);
    }

    @Test
    void getCondition_ShouldThrowAppException_WhenGeneralErrorOccurs() {
        when(conditionRepo.findById(1L)).thenThrow(new RuntimeException("Unexpected error"));

        AppException exception = assertThrows(AppException.class, () -> conditionService.getConditon(1L));

        assertTrue(exception.getMessage().contains("Unexpected error"));
    }
}
