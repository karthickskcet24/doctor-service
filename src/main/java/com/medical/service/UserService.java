package com.medical.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medical.constant.RoleType;
import com.medical.dto.ChatUserDto;
import com.medical.dto.GetConditionResponseDto;
import com.medical.dto.GetUserResponseDto;
import com.medical.dto.LoginDto;
import com.medical.dto.LoginResponseDto;
import com.medical.dto.PatientResponseDto;
import com.medical.dto.UpdateUserDto;
import com.medical.dto.UserDto;
import com.medical.entity.Condition;
import com.medical.entity.Message;
import com.medical.entity.Organization;
import com.medical.entity.Patient;
import com.medical.entity.Practitioner;
import com.medical.entity.User;
import com.medical.exception.AlreadyExistsException;
import com.medical.exception.AppException;
import com.medical.exception.NotFoundException;
import com.medical.repository.ConditionRepository;
import com.medical.repository.MessageRepository;
import com.medical.repository.OrganizationRepository;
import com.medical.repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class UserService {

	@Autowired
	UserRepository userRepo;

	@Autowired
	OrganizationRepository organizationRepo;

	@Autowired
	EntityManager entityManager;

	@Autowired
	ConditionRepository conditionRepo;

	@Autowired
	MessageRepository messageRepo;

	public static final String phoneRegexPattern = "^(\\+\\d{1,3}[- ]?)?\\d{10}$";

	public User updateUser(long userId, UpdateUserDto updateUserDto) {

		User user = userRepo.findById(userId).orElseThrow(() -> new NotFoundException("UserId not found"));

		if (user.getId() == updateUserDto.getUserId()) {
			throw new AppException("You dont have permission");
		}
		if (updateUserDto.getFirstName() != null && !updateUserDto.getFirstName().isEmpty()
				&& !updateUserDto.getFirstName().isBlank()) {
			user.setFirstName(updateUserDto.getFirstName());
		}
		if (updateUserDto.getLastName() != null && !updateUserDto.getLastName().isEmpty()
				&& !updateUserDto.getLastName().isBlank()) {
			user.setLastName(updateUserDto.getLastName());
		}
		if (updateUserDto.getAddress() != null && !updateUserDto.getAddress().isEmpty()
				&& !updateUserDto.getAddress().isBlank()) {
			user.setAddress(updateUserDto.getAddress());
		}
		if (updateUserDto.getContactInfo() != null && !updateUserDto.getContactInfo().isEmpty()
				&& !updateUserDto.getContactInfo().isBlank()) {
			if (updateUserDto.getContactInfo().matches(phoneRegexPattern)) {
				user.setContactInfo(updateUserDto.getContactInfo());
			}

		}
		if (updateUserDto.getGetDateOfBirth() != null && updateUserDto.getGetDateOfBirth().isBefore(LocalDate.now())) {

			user.setDateOfBirth(updateUserDto.getGetDateOfBirth());
		}
		return userRepo.save(user);
	}

	public List<GetUserResponseDto> viewAllUser(long userId, String userName) {
		try {

			List<GetUserResponseDto> responses = new ArrayList<>();

			List<Condition> conditions = conditionRepo.findAll();

			if (userName == null && userId > 0) {

				User userByRole = userRepo.findById(userId)
						.orElseThrow(() -> new NotFoundException("UserId not found"));
				if (userByRole.getRole().equalsIgnoreCase(RoleType.PATIENT.name())) {
					throw new NotFoundException("You cannot view All users");
				}

				List<User> users = userRepo.findAll();

				List<User> listOfUsers = users.stream().filter(e -> e.getRole().equals(RoleType.PATIENT.name()))
						.collect(Collectors.toList());

				for (User user : listOfUsers) {

					List<Condition> listOfConditions = conditions.stream()
							.filter(e -> user.getPatient() != null && e.getPatient() != null
									&& (e.getPatient().getId() == user.getPatient().getId()))
							.collect(Collectors.toList());
					responses.add(ConvertUserResponseDto(user, listOfConditions));
				}

				return responses;
			} else {
				CriteriaBuilder cb = entityManager.getCriteriaBuilder();
				CriteriaQuery<User> cq = cb.createQuery(User.class);
				Root<User> root = cq.from(User.class);
				List<Predicate> predicates = new ArrayList<>();

				if (userName != null && !userName.isEmpty()) {
					Predicate firstName = cb.like(cb.lower(root.get("firstName")), "%" + userName.toLowerCase() + "%");
					Predicate lastName = cb.like(cb.lower(root.get("lastName")), "%" + userName.toLowerCase() + "%");
					predicates.add(cb.or(firstName, lastName));
				}

				if (!predicates.isEmpty()) {
					cq.where(predicates.toArray(new Predicate[0]));
				}
				TypedQuery<User> query = entityManager.createQuery(cq);
				List<User> userSearchList = query.getResultList();

				List<User> listOfPatients = userSearchList.stream().sorted(Comparator.comparing(User::getFirstName))
						.filter(e -> e.getRole().equals(RoleType.PATIENT.name())).collect(Collectors.toList());

				for (User user : listOfPatients) {

					List<Condition> listOfConditions = conditions.stream().filter(
							e -> user.getPatient() != null && (e.getPatient().getId() == user.getPatient().getId()))
							.collect(Collectors.toList());
					responses.add(ConvertUserResponseDto(user, listOfConditions));

				}
				return responses;
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
	}

	public User getUser(long userId) {
		try {
			User user = userRepo.findById(userId).orElseThrow(() -> new NotFoundException("UserId not found!!!"));
			return user;
		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}
	}

	public List<User> getAllDoctorsByPatient(long userId) {

		try {

			User userByRole = userRepo.findById(userId).orElseThrow(() -> new NotFoundException("UserId not found"));
			if (!userByRole.getRole().equals(RoleType.PATIENT.name())) {
				throw new NotFoundException("Practitioner cannot view");
			}
			List<User> user = userRepo.findAll();

			List<User> listOfPractitioners = user.stream().filter(e -> !e.getRole().equals(RoleType.PATIENT.name()))
					.collect(Collectors.toList());

			return listOfPractitioners;

		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}

	}

	public List<ChatUserDto> getAllPatientForMessaged(long practitionerId) {
		try {
			User practitioner = userRepo.findById(practitionerId)
					.orElseThrow(() -> new NotFoundException("Practitioner id not found"));
			List<ChatUserDto> chatUserDtos = new ArrayList<>();
			if (practitioner != null && !practitioner.getRole().equals(RoleType.PATIENT.name())) {
				List<Message> practinerMessage = messageRepo.findAllByRecipientId(practitionerId);
				Set<User> users = practinerMessage.stream().map(e -> e.getSender()).collect(Collectors.toSet());
				users.forEach(user -> {
					ChatUserDto chatUserDto = new ChatUserDto();
					chatUserDto.setUser(user);
					chatUserDto.setCount(practinerMessage.stream()
							.filter(e -> e.getSender().getPatient().getId() == user.getPatient().getId()
									&& e.getRecipient().getId() == practitionerId && !e.isStatus())
							.count());
					Optional<LocalDateTime> first = practinerMessage.stream()
							.filter(e -> e.getRecipient().getPractitioner() != null
									&& e.getSender().getPatient().getId() == user.getPatient().getId()
									&& e.getRecipient().getId() == practitionerId && !e.isStatus())
							.sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt())).map(f -> f.getCreatedAt())
							.findFirst();
					if (first.isPresent()) {
						chatUserDto.setModifiedAt(first.get());
					}
					chatUserDtos.add(chatUserDto);
				});

				return chatUserDtos;
			} else {
				throw new NotFoundException("You don't hava a permission to see this information");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
	}

	public List<ChatUserDto> getAllPractiner(long patientId) {
		try {
			List<User> users = userRepo.findAll();
			List<User> notUsers = users.stream().filter(e -> e.getPractitioner() != null).collect(Collectors.toList());

			List<Message> patientMessages = messageRepo.findAllByRecipientIdAndStatus(patientId, false);

			List<ChatUserDto> chatUserDtos = new ArrayList<>();

			notUsers.forEach(user -> {

				ChatUserDto chatUserDto = new ChatUserDto();
				chatUserDto.setUser(user);

				long unreadMessageCount = patientMessages.stream().filter(
						message -> message.getSender().getPractitioner().getId() == user.getPractitioner().getId()
								&& message.getRecipient().getId() == patientId && !message.isStatus())
						.count();
				chatUserDto.setCount(unreadMessageCount);

				Optional<LocalDateTime> first = patientMessages.stream()
						.filter(e -> (e.getSender().getPractitioner().getId() == user.getPractitioner().getId()
								&& e.getRecipient().getId() == patientId) && !e.isStatus())
						.sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt())).map(f -> f.getCreatedAt())
						.findFirst();
				if (first.isPresent()) {
					chatUserDto.setModifiedAt(first.get());
				}
				chatUserDtos.add(chatUserDto);
			});

			return chatUserDtos;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
	}

	public GetUserResponseDto ConvertUserResponseDto(User user, List<Condition> conditions) {
		try {

			GetUserResponseDto userResponseDto = new GetUserResponseDto();

			userResponseDto.setId(user.getId());
			userResponseDto.setFirstName(user.getFirstName());
			userResponseDto.setLastName(user.getLastName());
			userResponseDto.setUsername(user.getUsername());
			userResponseDto.setAddress(user.getAddress());
			userResponseDto.setContactInfo(user.getContactInfo());
			userResponseDto.setGender(user.getGender());
			userResponseDto.setRole(user.getRole());
			userResponseDto.setDateOfBirth(user.getDateOfBirth());

			PatientResponseDto patientResponseDto = new PatientResponseDto();

			patientResponseDto.setId(user.getPatient().getId());

			List<GetConditionResponseDto> responses = new ArrayList<>();

			patientResponseDto.setConditions(responses);

			conditions.stream().forEach(e -> {

				GetConditionResponseDto getConditionResponseDto = new GetConditionResponseDto();

				getConditionResponseDto.setId(e.getId());
				getConditionResponseDto.setDateOfDiagnosis(e.getDateOfDiagnosis());
				getConditionResponseDto.setDiagnosis(e.getDiagnosis());
				getConditionResponseDto.setNotes(e.getNotes());
				getConditionResponseDto.setSeverity(e.getSeverity());

				responses.add(getConditionResponseDto);
			});
			userResponseDto.setConditions(responses);

			return userResponseDto;
		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}

	}

	public List<User> getAllDoctors() {
		List<User> users = userRepo.findAll();
		List<User> listOfUsers = users.stream().filter(e -> !e.getRole().equals(RoleType.PATIENT.name()))
				.collect(Collectors.toList());
		return listOfUsers;
	}

}
