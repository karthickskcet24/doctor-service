package com.medical.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medical.entity.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>{

}