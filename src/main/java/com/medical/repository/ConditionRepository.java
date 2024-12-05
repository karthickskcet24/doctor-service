package com.medical.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medical.entity.Condition;

@Repository
public interface ConditionRepository extends JpaRepository<Condition, Long> {

//	List<Condition> findAllByPatentIdId(Long id);
//
//	List<Condition> findAllByPatientIdId();

}
