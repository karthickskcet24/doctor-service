package com.medical.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medical.dto.DeleteMessageDto;
import com.medical.dto.MessageDto;
import com.medical.dto.MessageResponseDto;
import com.medical.entity.Message;
import com.medical.service.MessageService;

@RestController
@RequestMapping("/api/v1/message")
@CrossOrigin("*")
public class MessageController {

	@Autowired
	MessageService messageService;

	@PostMapping
	public ResponseEntity<Message> createMessage(@RequestBody MessageDto messageDto) {
		Message message = messageService.createMessage(messageDto);
		return new ResponseEntity<Message>(message, HttpStatus.CREATED);
	}

	@GetMapping("/{senderId}/{recipientId}")
	public ResponseEntity<List<MessageResponseDto>> getAllMessages(
			@PathVariable(name = "senderId") long senderId, @PathVariable(name = "recipientId") long recipientId) {
		List<MessageResponseDto> messages = messageService.getAllMessages(senderId, recipientId);
		return new ResponseEntity<List<MessageResponseDto>>(messages, HttpStatus.OK);
	}

	@PostMapping("/status/{senderId}/{recipientId}")
	public ResponseEntity<DeleteMessageDto> createStatus(@PathVariable("senderId")long senderId ,@PathVariable("recipientId")long recipientId) {
		String message = messageService.createStatus(senderId,recipientId);
		DeleteMessageDto messageDto = new DeleteMessageDto();
		messageDto.setMessgae(message);
		messageDto.setDateTime(LocalDateTime.now());
		return new ResponseEntity<DeleteMessageDto>(messageDto, HttpStatus.OK);
	}
	
	
	@GetMapping("/{practitionerId}")
	public ResponseEntity<List<MessageResponseDto>> getAllMessagesByPractitioner(
			@PathVariable(name = "practitionerId") long practitionerId) {

		List<MessageResponseDto> messages = messageService.getAllMessagesByPractitioner(practitionerId);

		return new ResponseEntity<List<MessageResponseDto>>(messages, HttpStatus.OK);
	}
	

	@DeleteMapping("/{messageId}/{senderId}")
	public ResponseEntity<DeleteMessageDto> deleteMessage(@PathVariable(name = "messageId") long messageId,
			@PathVariable(name = "senderId") long senderId) {

		DeleteMessageDto message = messageService.deleteMessage(messageId, senderId);

		return new ResponseEntity<DeleteMessageDto>(message, HttpStatus.OK);
	}
}
