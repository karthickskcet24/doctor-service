package com.medical.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponseDtoForUser {

	private String message;

	private long senderId;

	private long recipientId;

	private LocalDateTime createdAt;

	
}
