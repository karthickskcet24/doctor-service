package com.medical.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "encounter")
public class Encounter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDateTime createdAt;

	private LocalDateTime modifiedAt;

	private String reasonForVisit;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "patient_id")
	private Patient patient;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "practitioner_id")
	private Practitioner practitioner;

	@OneToMany(mappedBy = "encounter", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<Observation> observations;
	@JsonIgnore
	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Condition condition;

}
