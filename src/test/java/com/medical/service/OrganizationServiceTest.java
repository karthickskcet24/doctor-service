package com.medical.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.medical.dto.OrganizationDto;
import com.medical.dto.OrganizationResponseDto;
import com.medical.entity.Organization;
import com.medical.exception.AppException;
import com.medical.repository.OrganizationRepository;

@SpringBootTest
public class OrganizationServiceTest {

    @Mock
    private OrganizationRepository organizationRepo;

    @InjectMocks
    private OrganizationService organizationService;

    private OrganizationDto organizationDto;
    private Organization organization;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        organizationDto = new OrganizationDto();
        organizationDto.setName("Test Organization");
        organizationDto.setAddress("123 Test St");
        organizationDto.setPhone("+1234567890");

        organization = new Organization();
        organization.setId(1L);
        organization.setName("Test Organization");
        organization.setAddress("123 Test St");
        organization.setPhone("+1234567890");
    }

    @Test
    void testCreateOrganization_Success() {
        when(organizationRepo.findByName("Test Organization")).thenReturn(null);
        when(organizationRepo.save(any(Organization.class))).thenReturn(organization);

        Organization result = organizationService.createOrganization(organizationDto);

        assertNotNull(result);
        assertEquals("Test Organization", result.getName());
        assertEquals("123 Test St", result.getAddress());
        assertEquals("+1234567890", result.getPhone());
    }

    @Test
    void testCreateOrganization_AlreadyExists() {
        when(organizationRepo.findByName("Test Organization")).thenReturn(organization);

        assertThrows(AppException.class, () -> {
            organizationService.createOrganization(organizationDto);
        });
    }

    @Test
    void testGetOrganization_Success() {
        when(organizationRepo.findById(1L)).thenReturn(Optional.of(organization));

        OrganizationResponseDto result = organizationService.getOrganization(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Organization", result.getName());
    }

    @Test
    void testGetOrganization_NotFound() {
        when(organizationRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AppException.class, () -> {
            organizationService.getOrganization(1L);
        });
    }

    @Test
    void testUpdateOrganization_Success() {
        OrganizationDto updateDto = new OrganizationDto();
        updateDto.setName("Updated Organization");
        updateDto.setAddress("456 Updated St");
        updateDto.setPhone("+1234567890");

        when(organizationRepo.findById(1L)).thenReturn(Optional.of(organization));
        when(organizationRepo.save(any(Organization.class))).thenReturn(organization);

        Organization result = organizationService.updateOrganization(1L, updateDto);

        assertNotNull(result);
        assertEquals("Updated Organization", result.getName());
        assertEquals("456 Updated St", result.getAddress());
        assertEquals("+1234567890", result.getPhone());
    }

    @Test
    void testUpdateOrganization_NotFound() {
        OrganizationDto updateDto = new OrganizationDto();
        updateDto.setName("Updated Organization");

        when(organizationRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AppException.class, () -> {
            organizationService.updateOrganization(1L, updateDto);
        });
    }

    @Test
    void testGetAllOrganizations_Success() {
        List<Organization> organizations = List.of(organization);
        when(organizationRepo.findAll()).thenReturn(organizations);

        List<OrganizationResponseDto> result = organizationService.getAllOrganizations();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Organization", result.get(0).getName());
    }

    @Test
    void testGetAllOrganizations_NoOrganizations() {
        when(organizationRepo.findAll()).thenReturn(List.of());

        List<OrganizationResponseDto> result = organizationService.getAllOrganizations();

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testOrganizationEntityToDtoConverter_Success() {
        OrganizationResponseDto result = organizationService.organizationEntityToDtoConverter(organization);

        assertNotNull(result);
        assertEquals("Test Organization", result.getName());
        assertEquals("123 Test St", result.getAddress());
        assertEquals("+1234567890", result.getPhone());
    }

    @Test
    void testOrganizationEntityToDtoConverter_Exception() {
        assertThrows(AppException.class, () -> {
            organizationService.organizationEntityToDtoConverter(null);
        });
    }
}
