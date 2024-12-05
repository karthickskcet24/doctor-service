package com.medical.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SenderAndReciverMessageResponseDto {

	private List<MessageResponseDto> messages;
	
	private long unreadMessageCount;

}
