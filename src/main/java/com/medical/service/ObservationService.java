package com.medical.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medical.constant.RoleType;
import com.medical.dto.ObservationDto;
import com.medical.entity.Encounter;
import com.medical.entity.Location;
import com.medical.entity.Observation;
import com.medical.entity.User;
import com.medical.exception.AppException;
import com.medical.exception.NotFoundException;
import com.medical.repository.EncounterRepository;
import com.medical.repository.ObservationRepository;
import com.medical.repository.UserRepository;

@Service
public class ObservationService {
	@Autowired
	ObservationRepository observationRepo;

	@Autowired
	EncounterRepository encounterRepo;

	@Autowired
	UserRepository userRepo;

	public Observation createObservation(ObservationDto observationDto) {

		try {
			User user = userRepo.findById(observationDto.getPractitionerId())
					.orElseThrow(() -> new NotFoundException("UserId not found"));

			if (user.getRole().equalsIgnoreCase(RoleType.PATIENT.name())) {
				throw new NotFoundException("You don't have permission");
			}

			Encounter encounter = encounterRepo.findById(observationDto.getEncounterId())
					.orElseThrow(() -> new NotFoundException("EncounterId not found"));

			Observation observation = new Observation();
			observation.setCreatedAt(LocalDateTime.now());
			observation.setType(observationDto.getType());
			observation.setEncounter(encounter);
			observation.setPatient(encounter.getPatient());
			observation.setNotes(observationDto.getNotes());
			observation.setValue(observationDto.getValue());
			observation.setUnit(observationDto.getUnit());
			Location location = new Location();
			location.setName(observationDto.getName());
			location.setDepartment(observationDto.getDepartment());
			location.setAddress(observationDto.getAddress());
			observation.setLocation(location);
			observation.setPractitioner(user.getPractitioner());
			return observationRepo.save(observation);
		}

		catch (Exception e) {
			throw new AppException(e.getMessage());
		}
	}

	public Observation updateObservation(long observationId, ObservationDto observationDto) {

		try {
			System.out.println();
			User user = userRepo.findById(observationDto.getPractitionerId())
					.orElseThrow(() -> new NotFoundException("Practiner id not found"));

			if (user.getRole().equalsIgnoreCase(RoleType.PATIENT.name())) {
				throw new NotFoundException("You don't have permission");
			}
			Observation observation = observationRepo.findById(observationId)
					.orElseThrow(() -> new NotFoundException("ObservationId not found"));

			observation.setCreatedAt(LocalDateTime.now());
			if (observationDto.getType() != null && !observationDto.getType().isEmpty()
					&& !observationDto.getType().isBlank()) {
				observation.setType(observationDto.getType());
			}
			if (observationDto.getNotes() != null && !observationDto.getNotes().isEmpty()
					&& !observationDto.getNotes().isBlank()) {
				observation.setNotes(observationDto.getNotes());
			}
			if (observationDto.getValue() != null && !observationDto.getValue().isEmpty()
					&& !observationDto.getValue().isBlank()) {
				observation.setValue(observationDto.getValue());
			}
			if (observationDto.getUnit() != null && !observationDto.getUnit().isEmpty()
					&& !observationDto.getUnit().isBlank()) {
				observation.setUnit(observationDto.getUnit());
			}
			Location location = observation.getLocation();

			if (observationDto.getName() != null && !observationDto.getName().isEmpty()
					&& !observationDto.getName().isBlank()) {
				location.setName(observationDto.getName());
			}
			if (observationDto.getDepartment() != null && !observationDto.getDepartment().isEmpty()
					&& !observationDto.getDepartment().isBlank()) {
				location.setDepartment(observationDto.getDepartment());
			}

			if (observationDto.getAddress() != null && !observationDto.getAddress().isEmpty()
					&& !observationDto.getAddress().isBlank()) {
				location.setAddress(observationDto.getAddress());
			}
			if (user.getPractitioner() != null) {
				observation.setPractitioner(user.getPractitioner());
			}

			if (location != null) {
				observation.setLocation(location);
			}

			return observationRepo.save(observation);
		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}
	}

	public Observation getObservation(long observationId) {

		try {
			Observation observation = observationRepo.findById(observationId)
					.orElseThrow(() -> new NotFoundException("ObservationId not found"));

			return observation;
		}

		catch (Exception e) {
			throw new AppException(e.getMessage());
		}

	}

}
