package com.medical.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "message")
public class Message {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String message;

	private LocalDateTime createdAt;;

	private boolean status;

	@ManyToOne
	@JoinColumn(name = "sender_id")
	private User sender;

	@ManyToOne
	@JoinColumn(name = "recipient_id")
	private User recipient;

}
