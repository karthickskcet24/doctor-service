package com.medical.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.medical.constant.RoleType;
import com.medical.dto.DeleteMessageDto;
import com.medical.dto.MessageDto;
import com.medical.dto.MessageResponseDto;
import com.medical.entity.Message;
import com.medical.entity.User;
import com.medical.exception.AppException;
import com.medical.repository.MessageRepository;
import com.medical.repository.UserRepository;

class MessageServiceTest {

    @Mock
    private MessageRepository messageRepo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private MessageService messageService;

    private User sender;
    private User recipient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sender = new User();
        sender.setId(1L);
        sender.setRole(RoleType.DOCTOR.name());
        sender.setFirstName("John");

        recipient = new User();
        recipient.setId(2L);
        recipient.setRole(RoleType.PATIENT.name());
        recipient.setFirstName("Jane");
    }

    @Test
    void testCreateMessage_Success() {
        MessageDto messageDto = new MessageDto();
        messageDto.setSenderId(1L);
        messageDto.setRecipientId(2L);
        messageDto.setMessage("Hello");

        when(userRepo.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(sender, recipient));
        when(messageRepo.save(any(Message.class))).thenReturn(new Message());

        Message result = messageService.createMessage(messageDto);

        assertNotNull(result);
        verify(messageRepo, times(1)).save(any(Message.class));
    }

    @Test
    void testCreateMessage_Fail_NoPermission() {
        MessageDto messageDto = new MessageDto();
        messageDto.setSenderId(1L);
        messageDto.setRecipientId(2L);

        when(userRepo.findAllByIdIn(Arrays.asList(1L, 3L))).thenReturn(Arrays.asList(sender, sender));

        Exception exception = assertThrows(AppException.class, () -> messageService.createMessage(messageDto));
        assertEquals("SenderId or ReciverId not found", exception.getMessage());
    }

    @Test
    void testGetAllMessages_Success() {
        List<Message> messages = new ArrayList<>();
        Message message = new Message();
        message.setId(1L);
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setMessage("Test message");
        message.setCreatedAt(LocalDateTime.now());
        messages.add(message);

        when(messageRepo.findAllBySenderIdAndRecipientIdOrSenderIdAndRecipientId(1L, 2L, 2L, 1L))
            .thenReturn(messages);

        List<MessageResponseDto> result = messageService.getAllMessages(1L, 2L);

        assertEquals(1, result.size());
        assertEquals("Test message", result.get(0).getMessage());
    }

    @Test
    void testCreateStatus_Success() {
        List<Message> messages = new ArrayList<>();
        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setStatus(false);
        messages.add(message);

        when(messageRepo.findAllBySenderIdAndRecipientId(1L, 2L)).thenReturn(messages);
        when(messageRepo.saveAll(messages)).thenReturn(messages);

        String result = messageService.createStatus(1L, 2L);

        assertEquals("Message readed successfully...!", result);
    }

    @Test
    void testDeleteMessage_Success() {
        Message message = new Message();
        message.setId(1L);
        message.setSender(sender);

        when(messageRepo.findById(1L)).thenReturn(Optional.of(message));
        doNothing().when(messageRepo).deleteById(1L);

        DeleteMessageDto result = messageService.deleteMessage(1L, 1L);

        assertNotNull(result);
        assertEquals("Message delete sucessfully", result.getMessgae());
    }

    @Test
    void testDeleteMessage_Fail_NoPermission() {
        Message message = new Message();
        message.setId(1L);
        message.setSender(sender);

        when(messageRepo.findById(1L)).thenReturn(Optional.of(message));

        Exception exception = assertThrows(AppException.class, () -> messageService.deleteMessage(1L, 2L));
        assertEquals("You don't have permission", exception.getMessage());
    }

    @Test
    void testGetAllMessagesByPractitioner_Success() {
        List<Message> messages = new ArrayList<>();
        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setMessage("Test message");
        message.setCreatedAt(LocalDateTime.now());
        messages.add(message);

        when(messageRepo.findAllByRecipientId(2L)).thenReturn(messages);

        List<MessageResponseDto> result = messageService.getAllMessagesByPractitioner(2L);

        assertNotNull(result);
        assertEquals(0, result.size());
    }
}
