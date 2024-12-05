package com.medical.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medical.dto.OrganizationDto;
import com.medical.dto.OrganizationResponseDto;
import com.medical.entity.Organization;
import com.medical.exception.AlreadyExistsException;
import com.medical.exception.AppException;
import com.medical.exception.NotFoundException;
import com.medical.repository.OrganizationRepository;
import com.medical.repository.UserRepository;

@Service
public class OrganizationService {

	@Autowired
	OrganizationRepository organizationRepo;

	@Autowired
	UserRepository userRepo;

	public static final String phoneRegexPattern = "^(\\+\\d{1,3}[- ]?)?\\d{10}$";

	public Organization createOrganization(OrganizationDto organizationDto) {
		try {

			Organization organizationName = organizationRepo.findByName(organizationDto.getName());

			if (organizationName != null) {
				throw new AlreadyExistsException("This Organization is already exist!!!");
			}
			Organization organization = new Organization();

			organization.setName(organizationDto.getName());
			organization.setAddress(organizationDto.getAddress());
			organization.setPhone(organizationDto.getPhone());
			return organizationRepo.save(organization);
		}

		catch (Exception e) {
			throw new AppException(e.getMessage());
		}
	}

	public OrganizationResponseDto getOrganization(long organizationId) {
		try {

			Organization organization = organizationRepo.findById(organizationId)
					.orElseThrow(() -> new NotFoundException("OrganizationId not found"));
			OrganizationResponseDto organizationResponse = organizationEntityToDtoConverter(organization);

			return organizationResponse;
		}

		catch (Exception e) {
			throw new AppException(e.getMessage());
		}
	}

	public Organization updateOrganization(long organizationId, OrganizationDto organizationDto) {
		try {

			Organization organization = organizationRepo.findById(organizationId)
					.orElseThrow(() -> new NotFoundException("OrganizationId not found"));

			if (organizationDto.getName() != null && !organizationDto.getName().isEmpty()
					&& !organizationDto.getName().isBlank()) {
				organization.setName(organizationDto.getName());

			}
			if (organizationDto.getAddress() != null && !organizationDto.getAddress().isEmpty()
					&& !organizationDto.getAddress().isBlank()) {
				organization.setAddress(organizationDto.getAddress());
			}
			if (organizationDto.getPhone() != null && !organizationDto.getPhone().isEmpty()
					&& !organizationDto.getPhone().isBlank()) {
				if (organizationDto.getPhone().matches(phoneRegexPattern)) {
					organization.setPhone(organizationDto.getPhone());
				}

			}
			return organizationRepo.save(organization);
		}

		catch (Exception e) {
			throw new AppException(e.getMessage());
		}
	}

	public List<OrganizationResponseDto> getAllOrganizations() {
		try {
			List<Organization> organizatoins = organizationRepo.findAll();
			List<OrganizationResponseDto> organizatoinResponses = new ArrayList<>();
			organizatoins.stream().forEach(e -> {
				organizatoinResponses.add(organizationEntityToDtoConverter(e));
			});
			return organizatoinResponses;
		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}
	}

	public OrganizationResponseDto organizationEntityToDtoConverter(Organization organization) {

		try {
			OrganizationResponseDto organizationResponse = new OrganizationResponseDto();

			organizationResponse.setId(organization.getId());
			organizationResponse.setName(organization.getName());
			organizationResponse.setAddress(organization.getAddress());
			organizationResponse.setPhone(organization.getPhone());

			return organizationResponse;
		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}
	}
}
