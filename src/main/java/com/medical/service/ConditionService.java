package com.medical.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medical.constant.RoleType;
import com.medical.dto.ConditionDto;
import com.medical.dto.ConditionUpdateDto;
import com.medical.entity.Condition;
import com.medical.entity.Encounter;
import com.medical.entity.User;
import com.medical.exception.AppException;
import com.medical.exception.NotFoundException;
import com.medical.repository.ConditionRepository;
import com.medical.repository.EncounterRepository;
import com.medical.repository.UserRepository;

@Service
public class ConditionService {
	@Autowired
	UserRepository userRepo;

	@Autowired
	ConditionRepository conditionRepo;

	@Autowired
	EncounterRepository encounterRepo;

	public Condition createCondition(ConditionDto conditionDto) {
		try {

			Encounter encounter = encounterRepo.findById(conditionDto.getEncounterId())
					.orElseThrow(() -> new NotFoundException("EncounterId not found"));

			User user = userRepo.findById(conditionDto.getPractitionerId())

					.orElseThrow(() -> new NotFoundException("UserId not found"));

			if (user.getRole().equalsIgnoreCase(RoleType.PATIENT.name())) {
				throw new NotFoundException("You don't have permission");
			}

			Condition condition = new Condition();

			condition.setPractitioner(user.getPractitioner());
			condition.setPatient(encounter.getPatient());
			condition.setNotes(conditionDto.getNotes());
			condition.setDateOfDiagnosis(LocalDateTime.now());
			condition.setDiagnosis(conditionDto.getDiagnosis());
			condition.setSeverity(conditionDto.getSeverity());
			condition.setEncounter(encounter);
			encounter.setCondition(condition);
			return conditionRepo.save(condition);
		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}
	}

	public Condition updateCondition(long conditionId, ConditionUpdateDto conditionDto) {
		try {

			Condition condition = conditionRepo.findById(conditionId)
					.orElseThrow(() -> new NotFoundException("ConditionId not found"));

			User user = userRepo.findById(conditionDto.getPractitionerId())
					.orElseThrow(() -> new NotFoundException("PractitionerId not found"));

			if (user.getRole().equalsIgnoreCase(RoleType.PATIENT.name())) {
				throw new AppException("You dont have permission");
			}

			if (conditionDto.getNotes() != null || !conditionDto.getNotes().isEmpty()
					|| !conditionDto.getNotes().isBlank()) {
				condition.setNotes(conditionDto.getNotes());
			}

			condition.setDateOfDiagnosis(LocalDateTime.now());

			if (conditionDto.getDiagnosis() != null || !conditionDto.getDiagnosis().isEmpty()
					|| !conditionDto.getDiagnosis().isBlank()) {
				condition.setDiagnosis(conditionDto.getDiagnosis());
			}
			if (conditionDto.getSeverity() != null || !conditionDto.getSeverity().isEmpty()
					|| !conditionDto.getSeverity().isBlank()) {
				condition.setSeverity(conditionDto.getSeverity());
			}

			condition.setPractitioner(user.getPractitioner());

			return conditionRepo.save(condition);

		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}
	}

	public Condition getConditon(long conditionId) {
		try {
			Condition getCondition = conditionRepo.findById(conditionId)
					.orElseThrow(() -> new NotFoundException("ConditionId not found"));
			return getCondition;
		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}
	}

}
