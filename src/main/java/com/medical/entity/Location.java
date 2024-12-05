package com.medical.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "location")
public class Location {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(name = "address", length = 500)
	private String address;

	private String department;

//	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
//	@JoinColumn(name = "organization_id")
//	private Organization organization;

}
