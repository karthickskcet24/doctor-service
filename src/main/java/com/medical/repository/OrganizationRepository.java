package com.medical.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medical.entity.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

	Organization findByName(String name);

}
