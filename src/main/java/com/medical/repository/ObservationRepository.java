package com.medical.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medical.entity.Observation;

@Repository
public interface ObservationRepository extends JpaRepository<Observation, Long> {

}
