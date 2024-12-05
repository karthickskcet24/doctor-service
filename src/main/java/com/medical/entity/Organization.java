package com.medical.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "organization")
public class Organization {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(name = "address", length = 500)
	private String address;

	private String phone;
	@JsonIgnore
	@OneToMany(mappedBy = "organization", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<Practitioner> practitioners;

//	@OneToMany(mappedBy = "organization", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
//	private List<Location> locations;
}
