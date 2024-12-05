package com.medical.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medical.entity.Practitioner;

@Repository
public interface PractitionerRepository extends JpaRepository<Practitioner, Long> {

}
