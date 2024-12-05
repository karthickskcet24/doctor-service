package com.medical.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteMessageDto {

	private String messgae;

	private LocalDateTime dateTime;


}
