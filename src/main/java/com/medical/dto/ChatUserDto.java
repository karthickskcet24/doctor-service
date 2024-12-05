package com.medical.dto;

import java.time.LocalDateTime;

import com.medical.entity.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatUserDto {
	
	private User user;
	
	private  long count;
	
	private LocalDateTime modifiedAt;

}
