package com.medical.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medical.constant.RoleType;
import com.medical.dto.DeleteMessageDto;
import com.medical.dto.MessageDto;
import com.medical.dto.MessageResponseDto;
import com.medical.dto.MessageResponseDtoForUser;
import com.medical.dto.UserResponseDtoForMessage;
import com.medical.entity.Message;
import com.medical.entity.User;
import com.medical.exception.AppException;
import com.medical.exception.NotFoundException;
import com.medical.repository.MessageRepository;
import com.medical.repository.UserRepository;

@Service
public class MessageService {

	@Autowired
	MessageRepository messageRepo;

	@Autowired
	UserRepository userRepo;

	public Message createMessage(MessageDto messageDto) {

		try {
			List<Long> users = new ArrayList<>();

			users.add(messageDto.getSenderId());
			users.add(messageDto.getRecipientId());

			List<User> listOfUsers = userRepo.findAllByIdIn(users);

			if (listOfUsers == null || listOfUsers.isEmpty() || listOfUsers.size() != 2) {
				throw new NotFoundException("SenderId or ReciverId not found");
			}

			Message messgae = new Message();

			if ((listOfUsers.get(0).getRole().equals(RoleType.PATIENT.name())
					&& listOfUsers.get(1).getRole().equals(RoleType.PATIENT.name()))
					|| (listOfUsers.get(0).getRole().equals(RoleType.PRACTITIONER.name())
							&& listOfUsers.get(1).getRole().equals(RoleType.PRACTITIONER.name()))
					|| (listOfUsers.get(0).getRole().equals(RoleType.DOCTOR.name())
							&& listOfUsers.get(1).getRole().equals(RoleType.DOCTOR.name()))
					|| (listOfUsers.get(0).getRole().equals(RoleType.DOCTOR.name())
							&& listOfUsers.get(1).getRole().equals(RoleType.PRACTITIONER.name()))
					|| (listOfUsers.get(0).getRole().equals(RoleType.PRACTITIONER.name())
							&& listOfUsers.get(1).getRole().equals(RoleType.DOCTOR.name()))) {
				throw new AppException("You don't have permission");
			}

			listOfUsers.stream().forEach(e -> {

				if (e.getId() == messageDto.getSenderId()) {
					messgae.setSender(e);
				}
				if (e.getId() == messageDto.getRecipientId()) {
					messgae.setRecipient(e);
				}

			});

			messgae.setCreatedAt(LocalDateTime.now());
			messgae.setStatus(false);
			messgae.setMessage(messageDto.getMessage());

			return messageRepo.save(messgae);
		} catch (

		Exception e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
	}

	public List<MessageResponseDto> getAllMessages(long senderId, long recipientId) {

		try {
			
			List<Message> allMessages = messageRepo.findAllBySenderIdAndRecipientIdOrSenderIdAndRecipientId(senderId,
					recipientId, recipientId, senderId);
			
			List<MessageResponseDto> messages = new ArrayList<>();
			
			
			allMessages.stream().sorted(Comparator.comparing(Message::getCreatedAt)).forEach(e -> {

				MessageResponseDto message = new MessageResponseDto();
				message.setId(e.getId());
				message.setCreatedAt(e.getCreatedAt());
				message.setMessage(e.getMessage());
				message.setSenderId(e.getSender().getId());
				message.setRecipientId(e.getRecipient().getId());
				message.setAddress(e.getSender().getAddress());
				message.setContactInfo(e.getSender().getContactInfo());
				//message.setCreatedAt(e.getSender().getCreatedAt());
				message.setFirstName(e.getSender().getFirstName());
				message.setGender(e.getSender().getGender());
				message.setLastName(e.getSender().getLastName());
				message.setRole(e.getSender().getRole());
				message.setUsername(e.getSender().getUsername());
				message.setDateOfBirth(e.getSender().getDateOfBirth());
				messages.add(message);

			});
			return messages;
		}

		catch (

		Exception e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
	}

	public String createStatus(long sendId,long recipientId) {
		try {
			List<Message> allMessages = messageRepo.findAllBySenderIdAndRecipientId(sendId,
					recipientId);
			allMessages.forEach(k->{
				k.setStatus(true);
			});
			messageRepo.saveAll(allMessages);
			return "Message readed successfully...!";
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
	}


	
	public List<MessageResponseDto> getAllMessagesByPractitioner(long practitionerId) {
		try {

			List<Message> listOfRecipientIds = messageRepo.findAllByRecipientId(practitionerId);

			List<MessageResponseDto> listOfPractitionerIds = new ArrayList<>();

			UserResponseDtoForMessage userResponseDtoForMessage = new UserResponseDtoForMessage();

			List<MessageResponseDtoForUser> MessageResponseDtoForUser = new ArrayList<>();

			listOfRecipientIds.stream().forEach(e -> {
				userResponseDtoForMessage.setFirstName(e.getSender().getFirstName());
				MessageResponseDtoForUser message = new MessageResponseDtoForUser();
				message.setMessage(e.getMessage());
				MessageResponseDtoForUser.add(message);
				userResponseDtoForMessage.setListOfmessages(MessageResponseDtoForUser);
		});
			return listOfPractitionerIds;

		} catch (Exception e) {
			throw new AppException(e.getMessage());

		}

	}

	public DeleteMessageDto deleteMessage(long messageId, long senderId) {
		try {
			Message message = messageRepo.findById(messageId)
					.orElseThrow(() -> new NotFoundException("MessageId not found"));
			if (message.getSender().getId() != senderId) {
				throw new NotFoundException("You don't have permission");
			}
			messageRepo.deleteById(messageId);
			DeleteMessageDto response = new DeleteMessageDto();
			response.setDateTime(LocalDateTime.now());
			response.setMessgae("Message delete sucessfully");
			return response;
		}

		catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
	}

}
