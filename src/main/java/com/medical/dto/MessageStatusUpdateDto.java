package com.medical.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageStatusUpdateDto {

	private long messageId;

	private long recipientId;

	private boolean status;

}
