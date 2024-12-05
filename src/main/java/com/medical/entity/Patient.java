package com.medical.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "patient")
public class Patient {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JsonIgnore
	@OneToMany(mappedBy = "patient", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<Encounter> encounters;

	@JsonIgnore
	@OneToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	private User user;
}
