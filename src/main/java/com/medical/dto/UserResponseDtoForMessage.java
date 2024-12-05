package com.medical.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDtoForMessage {

	private String firstName;

	private String lastName;

	private LocalDate dateOfBirth;

	private String address;

	private String contactInfo;

	private String username;

	private String role;

	private String gender;
	
	private List<MessageResponseDtoForUser> listOfmessages;

}
