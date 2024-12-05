package com.medical.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.medical.dto.DeleteMessageDto;
import com.medical.dto.MessageDto;
import com.medical.dto.MessageResponseDto;
import com.medical.entity.Message;
import com.medical.entity.User;
import com.medical.service.MessageService;

@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

	@Mock
	private MessageService messageService;

	@InjectMocks
	private MessageController messageController;

	private MessageDto messageDto;
	private MessageResponseDto messageResponseDto;
	private Message message;
	private DeleteMessageDto deleteMessageDto;

	@BeforeEach
	void setUp() {
		messageDto = new MessageDto();
		messageDto.setMessage("Test Message");
		messageDto.setSenderId(1L);
		messageDto.setRecipientId(2L);

		messageResponseDto = new MessageResponseDto();
		messageResponseDto.setMessage("Test Message");
		messageResponseDto.setSenderId(1L);
		messageResponseDto.setRecipientId(2L);

		message = new Message();
		message.setId(1L);
		message.setMessage("Test Message");

		User sender = new User();
		sender.setId(1L);
		User recipient = new User();
		recipient.setId(1L);
		message.setSender(sender);
		message.setRecipient(recipient);

		deleteMessageDto = new DeleteMessageDto();
		deleteMessageDto.setMessgae("Message deleted successfully");
		deleteMessageDto.setDateTime(LocalDateTime.now());
	}

	@Test
	void createMessage_ShouldReturnCreatedStatus() {
		when(messageService.createMessage(messageDto)).thenReturn(message);

		ResponseEntity<Message> response = messageController.createMessage(messageDto);

		assertAll(() -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
				() -> assertEquals(message, response.getBody()),
				() -> verify(messageService, times(1)).createMessage(messageDto));
	}

	@Test
	void createMessage_ShouldThrowRuntimeException_WhenServiceFails() {
		when(messageService.createMessage(messageDto)).thenThrow(new RuntimeException("Service failed"));

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> messageController.createMessage(messageDto));

		assertEquals("Service failed", exception.getMessage());
	}

	@Test
	void getAllMessages_ShouldReturnOkStatus() {
		List<MessageResponseDto> messages = Arrays.asList(messageResponseDto);
		when(messageService.getAllMessages(1L, 2L)).thenReturn(messages);

		ResponseEntity<List<MessageResponseDto>> response = messageController.getAllMessages(1L, 2L);

		assertAll(() -> assertEquals(HttpStatus.OK, response.getStatusCode()),
				() -> assertEquals(messages, response.getBody()),
				() -> verify(messageService, times(1)).getAllMessages(1L, 2L));
	}

	@Test
	void getAllMessages_ShouldThrowRuntimeException_WhenServiceFails() {
		when(messageService.getAllMessages(1L, 2L)).thenThrow(new RuntimeException("Service failed"));

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> messageController.getAllMessages(1L, 2L));

		assertEquals("Service failed", exception.getMessage());
	}

	@Test
	void getAllMessages_ShouldReturnOk_WhenMessagesExist() {
		List<MessageResponseDto> messages = Arrays.asList(messageResponseDto);
		when(messageService.getAllMessages(1L, 2L)).thenReturn(messages);

		ResponseEntity<List<MessageResponseDto>> response = messageController.getAllMessages(1L, 2L);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void createStatus_ShouldReturnOkStatus() {
		when(messageService.createStatus(1L, 2L)).thenReturn("Status Created");

		ResponseEntity<DeleteMessageDto> response = messageController.createStatus(1L, 2L);

		assertAll(() -> assertEquals(HttpStatus.OK, response.getStatusCode()),
				() -> assertEquals("Status Created", response.getBody().getMessgae()));
	}

	@Test
	void createStatus_ShouldThrowRuntimeException_WhenServiceFails() {
		when(messageService.createStatus(1L, 2L)).thenThrow(new RuntimeException("Service failed"));

		RuntimeException exception = assertThrows(RuntimeException.class, () -> messageController.createStatus(1L, 2L));

		assertEquals("Service failed", exception.getMessage());
	}

	@Test
	void createStatus_ShouldReturnOk_WhenValidStatus() {
		when(messageService.createStatus(1L, 2L)).thenReturn("Status Created");

		ResponseEntity<DeleteMessageDto> response = messageController.createStatus(1L, 2L);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void deleteMessage_ShouldReturnOkStatus() {
		when(messageService.deleteMessage(1L, 1L)).thenReturn(deleteMessageDto);

		ResponseEntity<DeleteMessageDto> response = messageController.deleteMessage(1L, 1L);

		assertAll(() -> assertEquals(HttpStatus.OK, response.getStatusCode()),
				() -> assertEquals("Message deleted successfully", response.getBody().getMessgae()),
				() -> verify(messageService, times(1)).deleteMessage(1L, 1L));
	}

	@Test
	void deleteMessage_ShouldThrowRuntimeException_WhenServiceFails() {
		when(messageService.deleteMessage(1L, 1L)).thenThrow(new RuntimeException("Service failed"));

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> messageController.deleteMessage(1L, 1L));

		assertEquals("Service failed", exception.getMessage());
	}

	@Test
	void deleteMessage_ShouldReturnNotFound_WhenMessageNotFound() {
		when(messageService.deleteMessage(1L, 1L)).thenThrow(new RuntimeException("Message not found"));

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> messageController.deleteMessage(1L, 1L));

		assertEquals("Message not found", exception.getMessage());
	}

	@Test
	void deleteMessage_ShouldReturnOk_WhenMessageDeleted() {
		when(messageService.deleteMessage(1L, 1L)).thenReturn(deleteMessageDto);

		ResponseEntity<DeleteMessageDto> response = messageController.deleteMessage(1L, 1L);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
}
