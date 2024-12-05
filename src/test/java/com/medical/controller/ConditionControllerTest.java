package com.medical.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.medical.dto.ConditionDto;
import com.medical.dto.ConditionUpdateDto;
import com.medical.entity.Condition;
import com.medical.exception.NotFoundException;
import com.medical.service.ConditionService;

@ExtendWith(MockitoExtension.class)
class ConditionControllerTest {

    @Mock
    private ConditionService conditionService;

    @InjectMocks
    private ConditionController conditionController;

    private ConditionDto conditionDto;
    private ConditionUpdateDto conditionUpdateDto;
    private Condition condition;

    @BeforeEach
    void setUp() {
        conditionDto = new ConditionDto();
        conditionDto.setDiagnosis("Test Diagnosis");
        conditionDto.setSeverity("High");
        conditionDto.setNotes("Test notes");
        conditionDto.setPractitionerId(1L);
        conditionDto.setEncounterId(1L);

        conditionUpdateDto = new ConditionUpdateDto();
        conditionUpdateDto.setDiagnosis("Updated Diagnosis");
        conditionUpdateDto.setSeverity("Low");
        conditionUpdateDto.setNotes("Updated notes");
        conditionUpdateDto.setPractitionerId(1L);

        condition = new Condition();
        condition.setId(1L);
        condition.setDiagnosis("Test Diagnosis");
        condition.setSeverity("High");
        condition.setNotes("Test notes");
    }

    @Test
    void createCondition_ShouldReturnCreatedStatus() {
        when(conditionService.createCondition(conditionDto)).thenReturn(condition);

        ResponseEntity<Condition> response = conditionController.createCondition(conditionDto);

        assertAll(
            () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
            () -> assertEquals(condition, response.getBody()),
            () -> verify(conditionService, times(1)).createCondition(conditionDto)
        );
    }

    @Test
    void createCondition_ShouldThrowRuntimeException_WhenServiceFails() {
        when(conditionService.createCondition(conditionDto)).thenThrow(new RuntimeException("Service failed"));

        RuntimeException exception = assertThrows(
            RuntimeException.class, 
            () -> conditionController.createCondition(conditionDto)
        );

        assertEquals("Service failed", exception.getMessage());
    }

    @Test
    void updateCondition_ShouldReturnAcceptedStatus() {
        when(conditionService.updateCondition(1L, conditionUpdateDto)).thenReturn(condition);

        ResponseEntity<Condition> response = conditionController.updateCondition(1L, conditionUpdateDto);

        assertAll(
            () -> assertEquals(HttpStatus.ACCEPTED, response.getStatusCode()),
            () -> assertEquals(condition, response.getBody()),
            () -> verify(conditionService, times(1)).updateCondition(1L, conditionUpdateDto)
        );
    }

    @Test
    void updateCondition_ShouldThrowNotFoundException_WhenConditionDoesNotExist() {
        when(conditionService.updateCondition(1L, conditionUpdateDto)).thenThrow(new NotFoundException("Condition not found"));

        NotFoundException exception = assertThrows(
            NotFoundException.class, 
            () -> conditionController.updateCondition(1L, conditionUpdateDto)
        );

        assertEquals("Condition not found", exception.getMessage());
    }

    @Test
    void updateCondition_ShouldThrowRuntimeException_WhenServiceFails() {
        when(conditionService.updateCondition(1L, conditionUpdateDto)).thenThrow(new RuntimeException("Service failed"));

        RuntimeException exception = assertThrows(
            RuntimeException.class, 
            () -> conditionController.updateCondition(1L, conditionUpdateDto)
        );

        assertEquals("Service failed", exception.getMessage());
    }

    @Test
    void getCondition_ShouldReturnOkStatus() {
        when(conditionService.getConditon(1L)).thenReturn(condition);

        ResponseEntity<Condition> response = conditionController.getCondition(1L);

        assertAll(
            () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
            () -> assertEquals(condition, response.getBody()),
            () -> verify(conditionService, times(1)).getConditon(1L)
        );
    }

    @Test
    void getCondition_ShouldThrowNotFoundException_WhenConditionDoesNotExist() {
        long conditionId = 1L;
        when(conditionService.getConditon(conditionId)).thenThrow(new NotFoundException("Condition not found"));

        NotFoundException exception = assertThrows(
            NotFoundException.class, 
            () -> conditionController.getCondition(conditionId)
        );

        assertEquals("Condition not found", exception.getMessage());
    }

    @Test
    void getCondition_ShouldThrowRuntimeException_WhenServiceFails() {
        long conditionId = 1L;
        when(conditionService.getConditon(conditionId)).thenThrow(new RuntimeException("Service failed"));

        RuntimeException exception = assertThrows(
            RuntimeException.class, 
            () -> conditionController.getCondition(conditionId)
        );

        assertEquals("Service failed", exception.getMessage());
    }
}
