package com.medical.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String firstName;

	private String lastName;
	
	private LocalDate dateOfBirth;

	@Column(name = "address", length = 500)
	private String address;

	private String contactInfo;
	
	private String username;

	@JsonIgnore
	private String password;

	private String role;
	
	private String gender;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Patient patient;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Practitioner practitioner;

	private LocalDateTime createdAt;

}
