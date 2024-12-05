package com.medical.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medical.entity.Encounter;

@Repository
public interface EncounterRepository extends JpaRepository<Encounter, Long> {

	Encounter findByIdAndPractitionerId(long encounterId, long userId);


	List<Encounter> findAllByPatientUserId(long userId);

}
