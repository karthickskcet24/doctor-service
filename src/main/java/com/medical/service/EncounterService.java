package com.medical.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medical.constant.RoleType;
import com.medical.dto.CondtionResponseDto;
import com.medical.dto.EncounterDto;
import com.medical.dto.EncounterResponseDto;
import com.medical.dto.EncounterUpdateDto;
import com.medical.dto.LocationResponseDto;
import com.medical.dto.ObservationResponseDto;
import com.medical.dto.OrganizationResponseDto;
import com.medical.dto.PractitionerResponseDto;
import com.medical.dto.UserResponseDto;
import com.medical.entity.Encounter;
import com.medical.entity.Practitioner;
import com.medical.entity.User;
import com.medical.exception.AppException;
import com.medical.exception.NotFoundException;
import com.medical.repository.EncounterRepository;
import com.medical.repository.PatientRepository;
import com.medical.repository.UserRepository;

@Service
public class EncounterService {

	@Autowired
	EncounterRepository encounterRepo;

	@Autowired
	PatientRepository patientRepo;

	@Autowired
	UserRepository userRepo;

	public Encounter createEncounter(EncounterDto encounterDto) {

		try {

			List<Long> users = new ArrayList<>();
			users.add(encounterDto.getPractitionerId());
			users.add(encounterDto.getPatientId());
			List<User> listOfUsers = userRepo.findAllByIdIn(users);

			if (listOfUsers == null || listOfUsers.isEmpty() || listOfUsers.size() != 2) {
				throw new NotFoundException("PatientId or PractitionerId not found");
			}

			for (User user : listOfUsers) {
				if (user.getId() == encounterDto.getPatientId() && !user.getRole().equals(RoleType.PATIENT.name())) {
					throw new NotFoundException("PatientId not found");
				} else if (user.getId() == encounterDto.getPractitionerId()
						&& (!(user.getRole().equals(RoleType.DOCTOR.name()))
								&& !(user.getRole().equals(RoleType.PRACTITIONER.name())))) {
					throw new NotFoundException("PractitionerId not found");
				}
			}

			Encounter encounter = new Encounter();

			listOfUsers.stream().forEach(e -> {
				if (e.getId() == encounterDto.getPractitionerId()
						&& (e.getRole().equalsIgnoreCase(RoleType.PRACTITIONER.name())
								|| e.getRole().equalsIgnoreCase(RoleType.DOCTOR.name()))) {
					encounter.setPractitioner(e.getPractitioner());
					List<Encounter> encounters = new ArrayList<>();
					encounters.add(encounter);
					e.getPractitioner().setEncounters(encounters);

				} else if (e.getId() == encounterDto.getPatientId()
						&& e.getRole().equalsIgnoreCase(RoleType.PATIENT.name())) {
					encounter.setPatient(e.getPatient());
					List<Encounter> encounters = new ArrayList<>();
					encounters.add(encounter);
					e.getPatient().setEncounters(encounters);
				}

			});

			encounter.setCreatedAt(LocalDateTime.now());
			encounter.setModifiedAt(LocalDateTime.now());

			encounter.setReasonForVisit(encounterDto.getReasonForVisit());

			return encounterRepo.save(encounter);
		}

		catch (Exception e) {
			throw new AppException(e.getMessage());
		}
	}

	public Encounter updateEncounter(long encounterId, EncounterUpdateDto encounterDto) {

		try {
			User user = userRepo.findById(encounterDto.getPractitionerId())
					.orElseThrow(() -> new NotFoundException("UserId not found"));

			if (user.getRole().equalsIgnoreCase(RoleType.PATIENT.name())) {
				throw new NotFoundException("You don't have permission");
			}

			Encounter encounter = encounterRepo.findById(encounterId)
					.orElseThrow(() -> new NotFoundException("EncounterId not found"));

			encounter.setModifiedAt(LocalDateTime.now());
			if (encounterDto.getReasonForVisit() != null && !encounterDto.getReasonForVisit().isEmpty()
					&& !encounterDto.getReasonForVisit().isBlank()) {
				encounter.setReasonForVisit(encounterDto.getReasonForVisit());
			}
			return encounterRepo.save(encounter);
		}

		catch (Exception e) {
			throw new AppException(e.getMessage());
		}
	}

	public EncounterResponseDto getEncounter(long encounterId) {

		try {

			Encounter encounter = encounterRepo.findById(encounterId)
					.orElseThrow(() -> new NotFoundException("EncounterId not found"));
			EncounterResponseDto encounterResponseDto = EncuonterEntityToDtoConverter(encounter);

			return encounterResponseDto;
		}

		catch (Exception e) {
			throw new AppException(e.getMessage());
		}
	}

//	public List<EncounterResponseDto> getAllEncounters(long userId) {
//
//		try {
//			List<Encounter> patients = encounterRepo.findAllByPatientUserId(userId);
//			
//			List<EncounterResponseDto> responses = new ArrayList<>();
//			
//			patients.stream().forEach(e -> {
//				responses.add(EncuonterEntityToDtoConverter(e));
//
//			});
//
//			return responses;
//		} catch (Exception e) {
//			throw new AppException(e.getMessage());
//		}
//
//	}
	
	public List<EncounterResponseDto> getAllEncounters(long userId) {

		try {
			List<Encounter> patients = encounterRepo.findAllByPatientUserId(userId);

			List<EncounterResponseDto> responses = new ArrayList<>();

			patients.stream().sorted(Comparator.comparing(Encounter::getCreatedAt).reversed()).forEach(e -> {
				responses.add(EncuonterEntityToDtoConverter(e));
			});

			return responses;
		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}

	}


	public EncounterResponseDto EncuonterEntityToDtoConverter(Encounter encounter) {
		try {
			EncounterResponseDto encounterResponse = new EncounterResponseDto();
			
			encounterResponse.setId(encounter.getId());
			encounterResponse.setCreatedAt(encounter.getCreatedAt());

			UserResponseDto patient = new UserResponseDto();
			
			patient.setAddress(encounter.getPatient().getUser().getAddress());
			patient.setDateOfBirth(encounter.getPatient().getUser().getDateOfBirth());
			patient.setContactInfo(encounter.getPatient().getUser().getContactInfo());
			patient.setFirstName(encounter.getPatient().getUser().getFirstName());
			patient.setGender(encounter.getPatient().getUser().getGender());
			patient.setId(encounter.getPatient().getId());
			patient.setLastName(encounter.getPatient().getUser().getLastName());
			patient.setRole(encounter.getPatient().getUser().getRole());
			patient.setUsername(encounter.getPatient().getUser().getUsername());
			patient.setUserId(encounter.getPatient().getUser().getId());
			
			encounterResponse.setPatient(patient);
			encounterResponse.setModifiedAt(encounter.getModifiedAt());
			encounterResponse.setPractitioner(convertToPractitionerDto(encounter.getPractitioner()));
			 
			if (encounter.getCondition() != null) {
				CondtionResponseDto condtion = new CondtionResponseDto();
				condtion.setSeverity(encounter.getCondition().getSeverity());
				condtion.setId(encounter.getCondition().getId());
				condtion.setDiagnosis(encounter.getCondition().getDiagnosis());
				condtion.setNotes(encounter.getCondition().getNotes());
				condtion.setDateOfDiagnosis(encounter.getCondition().getDateOfDiagnosis());
				condtion.setPractitioner(convertToPractitionerDto(encounter.getCondition().getPractitioner()));
				encounterResponse.setCondition(condtion);
			}

			encounterResponse.setReasonForVisit(encounter.getReasonForVisit());
			
			List<ObservationResponseDto> responses = new ArrayList<>();
			
			encounter.getObservations().stream().forEach(e -> {
				
				ObservationResponseDto response = new ObservationResponseDto();
				response.setId(e.getId());
				response.setNotes(e.getNotes());
				response.setType(e.getType());
				response.setUnit(e.getUnit());
				response.setValue(e.getValue());
				response.setCreatedAt(e.getCreatedAt());
				
				response.setPractitioner(convertToPractitionerDto(e.getPractitioner()));
				
				LocationResponseDto location = new LocationResponseDto();
				location.setId(e.getLocation().getId());
				location.setName(e.getLocation().getName());
				location.setDepartment(e.getLocation().getDepartment());
				location.setAddress(e.getLocation().getAddress());
				response.setLocation(location);
				responses.add(response);

			});
			encounterResponse.setObservations(responses);
			return encounterResponse;
		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}

	}

	private PractitionerResponseDto convertToPractitionerDto(Practitioner practitioner) {
		PractitionerResponseDto practitionerResponseDto = new PractitionerResponseDto();
		practitionerResponseDto.setAddress(practitioner.getUser().getAddress());
		practitionerResponseDto.setDateOfBirth(practitioner.getUser().getDateOfBirth());
		practitionerResponseDto.setContactInfo(practitioner.getUser().getContactInfo());
		practitionerResponseDto.setFirstName(practitioner.getUser().getFirstName());
		practitionerResponseDto.setGender(practitioner.getUser().getGender());
		practitionerResponseDto.setId(practitioner.getId());
		practitionerResponseDto.setLastName(practitioner.getUser().getLastName());
		practitionerResponseDto.setRole(practitioner.getUser().getRole());
		practitionerResponseDto.setSpecialty(practitioner.getSpecialty());
		
		OrganizationResponseDto organizationResponse = new OrganizationResponseDto();
		
		organizationResponse.setId(practitioner.getOrganization().getId());
		organizationResponse.setName(practitioner.getOrganization().getName());
		organizationResponse.setAddress(practitioner.getOrganization().getAddress());
		organizationResponse.setPhone(practitioner.getOrganization().getPhone());
		practitionerResponseDto.setOrganization(organizationResponse);
		practitionerResponseDto.setUserId(practitioner.getUser().getId());
		practitionerResponseDto.setUsername(practitioner.getUser().getUsername());
		return practitionerResponseDto;
	}
}




