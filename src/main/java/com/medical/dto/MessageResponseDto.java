package com.medical.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponseDto {
	
	private long id;

	private String message;

	private long senderId;

	private long recipientId;

	private LocalDateTime createdAt;

	private String firstName;

	private String lastName;

	private LocalDate dateOfBirth;

	private String address;

	private String contactInfo;

	private String username;

	private String role;

	private String gender;
}
