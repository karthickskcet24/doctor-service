package com.medical.controller;

import com.medical.dto.ObservationDto;
import com.medical.entity.Observation;
import com.medical.service.ObservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObservationControllerTest {

    @Mock
    private ObservationService observationService;

    @InjectMocks
    private ObservationController observationController;

    private ObservationDto observationDto;
    private Observation observation;

    @BeforeEach
    void setUp() {
        observationDto = new ObservationDto();
        observationDto.setType("Blood Pressure");
        observationDto.setValue("120/80");
        observationDto.setUnit("mmHg");
        observationDto.setNotes("Normal");
        observationDto.setName("Patient1");
        observationDto.setAddress("123 Street");
        observationDto.setDepartment("Cardiology");
        observationDto.setPractitionerId(1L);
        observationDto.setEncounterId(1L);

        observation = new Observation();
        observation.setId(1L);
        observation.setType("Blood Pressure");
        observation.setValue("120/80");
        observation.setUnit("mmHg");
        observation.setNotes("Normal");
    }

    @Test
    void createObservation_ShouldReturnCreatedStatus() {
        when(observationService.createObservation(observationDto)).thenReturn(observation);

        ResponseEntity<Observation> response = observationController.createObservation(observationDto);

        assertAll(
            () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
            () -> assertEquals(observation, response.getBody()),
            () -> verify(observationService, times(1)).createObservation(observationDto)
        );
    }

    @Test
    void createObservation_ShouldThrowRuntimeException_WhenServiceFails() {
        when(observationService.createObservation(observationDto)).thenThrow(new RuntimeException("Service failed"));

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> observationController.createObservation(observationDto)
        );

        assertEquals("Service failed", exception.getMessage());
    }

    @Test
    void getObservation_ShouldReturnOkStatus() {
        when(observationService.getObservation(1L)).thenReturn(observation);

        ResponseEntity<Observation> response = observationController.getObservation(1L);

        assertAll(
            () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
            () -> assertEquals(observation, response.getBody()),
            () -> verify(observationService, times(1)).getObservation(1L)
        );
    }

    @Test
    void getObservation_ShouldThrowRuntimeException_WhenServiceFails() {
        when(observationService.getObservation(1L)).thenThrow(new RuntimeException("Service failed"));

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> observationController.getObservation(1L)
        );

        assertEquals("Service failed", exception.getMessage());
    }

    @Test
    void getObservation_ShouldReturnOk_WhenValidObservation() {
        when(observationService.getObservation(1L)).thenReturn(observation);

        ResponseEntity<Observation> response = observationController.getObservation(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateObservation_ShouldReturnAcceptedStatus() {
        when(observationService.updateObservation(1L, observationDto)).thenReturn(observation);

        ResponseEntity<Observation> response = observationController.updateObservation(1L, observationDto);

        assertAll(
            () -> assertEquals(HttpStatus.ACCEPTED, response.getStatusCode()),
            () -> assertEquals(observation, response.getBody()),
            () -> verify(observationService, times(1)).updateObservation(1L, observationDto)
        );
    }

    @Test
    void updateObservation_ShouldThrowRuntimeException_WhenServiceFails() {
        when(observationService.updateObservation(1L, observationDto)).thenThrow(new RuntimeException("Service failed"));

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> observationController.updateObservation(1L, observationDto)
        );

        assertEquals("Service failed", exception.getMessage());
    }

}
