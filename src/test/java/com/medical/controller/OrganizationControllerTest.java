package com.medical.controller;

import com.medical.dto.OrganizationDto;
import com.medical.dto.OrganizationResponseDto;
import com.medical.entity.Organization;
import com.medical.service.OrganizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationControllerTest {

    @Mock
    private OrganizationService organizationService;

    @InjectMocks
    private OrganizationController organizationController;

    private OrganizationDto organizationDto;
    private Organization organization;
    private OrganizationResponseDto organizationResponseDto;

    @BeforeEach
    void setUp() {
        organizationDto = new OrganizationDto();
        organizationDto.setName("HealthCare Inc.");
        organizationDto.setAddress("123 Healthcare St.");
        organizationDto.setPhone("+1234567890");

        organization = new Organization();
        organization.setId(1L);
        organization.setName("HealthCare Inc.");
        organization.setAddress("123 Healthcare St.");
        organization.setPhone("+1234567890");

        organizationResponseDto = new OrganizationResponseDto();
        organizationResponseDto.setName("HealthCare Inc.");
        organizationResponseDto.setAddress("123 Healthcare St.");
        organizationResponseDto.setPhone("+1234567890");
    }

    @Test
    void createOrganization_ShouldReturnCreatedStatus() {
        when(organizationService.createOrganization(organizationDto)).thenReturn(organization);

        ResponseEntity<Organization> response = organizationController.createOrganization(organizationDto);

        assertAll(
            () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
            () -> assertEquals(organization, response.getBody()),
            () -> verify(organizationService, times(1)).createOrganization(organizationDto)
        );
    }

    @Test
    void createOrganization_ShouldThrowRuntimeException_WhenServiceFails() {
        when(organizationService.createOrganization(organizationDto)).thenThrow(new RuntimeException("Service failed"));

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> organizationController.createOrganization(organizationDto)
        );

        assertEquals("Service failed", exception.getMessage());
    }

    @Test
    void getOrganization_ShouldReturnOkStatus() {
        when(organizationService.getOrganization(1L)).thenReturn(organizationResponseDto);

        ResponseEntity<OrganizationResponseDto> response = organizationController.getOrganization(1L);

        assertAll(
            () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
            () -> assertEquals(organizationResponseDto, response.getBody()),
            () -> verify(organizationService, times(1)).getOrganization(1L)
        );
    }

    @Test
    void getOrganization_ShouldThrowRuntimeException_WhenServiceFails() {
        when(organizationService.getOrganization(1L)).thenThrow(new RuntimeException("Service failed"));

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> organizationController.getOrganization(1L)
        );

        assertEquals("Service failed", exception.getMessage());
    }

    @Test
    void getOrganization_ShouldReturnOk_WhenValidOrganization() {
        when(organizationService.getOrganization(1L)).thenReturn(organizationResponseDto);

        ResponseEntity<OrganizationResponseDto> response = organizationController.getOrganization(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAllOrganizations_ShouldReturnOkStatus() {
        when(organizationService.getAllOrganizations()).thenReturn(List.of(organizationResponseDto));

        ResponseEntity<List<OrganizationResponseDto>> response = organizationController.getAllOrganization();

        assertAll(
            () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
            () -> assertEquals(1, response.getBody().size()),
            () -> verify(organizationService, times(1)).getAllOrganizations()
        );
    }

    @Test
    void getAllOrganizations_ShouldReturnEmptyList_WhenNoOrganizations() {
        when(organizationService.getAllOrganizations()).thenReturn(List.of());

        ResponseEntity<List<OrganizationResponseDto>> response = organizationController.getAllOrganization();

        assertAll(
            () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
            () -> assertEquals(0, response.getBody().size())
        );
    }

    @Test
    void getAllOrganizations_ShouldThrowRuntimeException_WhenServiceFails() {
        when(organizationService.getAllOrganizations()).thenThrow(new RuntimeException("Service failed"));

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> organizationController.getAllOrganization()
        );

        assertEquals("Service failed", exception.getMessage());
    }

    @Test
    void getAllOrganizations_ShouldReturnOk_WhenOrganizationsExist() {
        when(organizationService.getAllOrganizations()).thenReturn(List.of(organizationResponseDto));

        ResponseEntity<List<OrganizationResponseDto>> response = organizationController.getAllOrganization();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateOrganization_ShouldReturnAcceptedStatus() {
        when(organizationService.updateOrganization(1L, organizationDto)).thenReturn(organization);

        ResponseEntity<Organization> response = organizationController.updateOrganization(1L, organizationDto);

        assertAll(
            () -> assertEquals(HttpStatus.ACCEPTED, response.getStatusCode()),
            () -> assertEquals(organization, response.getBody()),
            () -> verify(organizationService, times(1)).updateOrganization(1L, organizationDto)
        );
    }


    @Test
    void updateOrganization_ShouldThrowRuntimeException_WhenServiceFails() {
        when(organizationService.updateOrganization(1L, organizationDto)).thenThrow(new RuntimeException("Service failed"));

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> organizationController.updateOrganization(1L, organizationDto)
        );

        assertEquals("Service failed", exception.getMessage());
    }

}
