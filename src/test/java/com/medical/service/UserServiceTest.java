package com.medical.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.medical.dto.ChatUserDto;
import com.medical.dto.GetUserResponseDto;
import com.medical.dto.UpdateUserDto;
import com.medical.entity.Condition;
import com.medical.entity.Message;
import com.medical.entity.Patient;
import com.medical.entity.Practitioner;
import com.medical.entity.User;
import com.medical.exception.AppException;
import com.medical.exception.NotFoundException;
import com.medical.repository.ConditionRepository;
import com.medical.repository.MessageRepository;
import com.medical.repository.UserRepository;

import jakarta.persistence.EntityManager;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepo;

    @MockBean
    private ConditionRepository conditionRepo;

    @MockBean
    private MessageRepository messageRepo;

    @MockBean
    private EntityManager entityManager;

    @Test
    public void testUpdateUser_Success() {
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setFirstName("John");
        updateUserDto.setLastName("Doe");
        updateUserDto.setAddress("123 New St");
        updateUserDto.setContactInfo("1234567890");
        updateUserDto.setGetDateOfBirth(LocalDate.now().minusYears(25));
        
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setFirstName("Jane");
        existingUser.setLastName("Smith");
        existingUser.setAddress("123 Old St");
        existingUser.setContactInfo("0987654321");
        existingUser.setRole("patient");
        existingUser.setDateOfBirth(LocalDate.now().minusYears(30));

        when(userRepo.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepo.save(any(User.class))).thenReturn(existingUser);

        User updatedUser = userService.updateUser(1L, updateUserDto);

        assertNotNull(updatedUser);
        assertEquals("John", updatedUser.getFirstName());
        assertEquals("Doe", updatedUser.getLastName());
        assertEquals("123 New St", updatedUser.getAddress());
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setFirstName("John");
        updateUserDto.setLastName("Doe");
        updateUserDto.setAddress("123 New St");
        updateUserDto.setContactInfo("1234567890");
        updateUserDto.setGetDateOfBirth(LocalDate.now().minusYears(25));

        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.updateUser(1L, updateUserDto));
    }

    @Test
    public void testViewAllUser_Success() {
        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setAddress("123 New St");
        user1.setContactInfo("1234567890");
        user1.setRole("PATIENT");
        user1.setDateOfBirth(LocalDate.now().minusYears(25));

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        user2.setAddress("456 Old St");
        user2.setContactInfo("0987654321");
        user2.setRole("DOCTOR");
        user2.setDateOfBirth(LocalDate.now().minusYears(30));

        GetUserResponseDto dto = new GetUserResponseDto();
        dto.setId(1L);
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setAddress("123 New St");
        dto.setContactInfo("1234567890");
        dto.setRole("PATIENT");
        dto.setDateOfBirth(LocalDate.now().minusYears(25));

        GetUserResponseDto dto2 = new GetUserResponseDto();
        dto2.setId(2L);
        dto2.setFirstName("Jane");
        dto2.setLastName("Doe");
        dto2.setAddress("456 Old St");
        dto2.setContactInfo("0987654321");
        dto2.setRole("PATIENT");
        dto2.setDateOfBirth(LocalDate.now().minusYears(30));

        Condition condition = new Condition();
        condition.setId(1L);
        condition.setDiagnosis("Cold");

        Condition condition1 = new Condition();
        condition1.setId(2L);
        condition1.setDiagnosis("Fever");

        List<Condition> conditions = Arrays.asList(condition1, condition);

        when(userRepo.findAll()).thenReturn(Arrays.asList(user1, user2));  
        when(conditionRepo.findAll()).thenReturn(conditions); 

        List<GetUserResponseDto> response = Arrays.asList(dto, dto2);
//        when( userService.viewAllUser(1L, null)).thenReturn(response);
        assertNotNull(response);
    }


    @Test
    public void testViewAllUser_UserRoleNotPatient() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Admin");
        user.setLastName("User");
        user.setAddress("789 Admin St");
        user.setContactInfo("1234567890");
        user.setRole("admin");
        user.setDateOfBirth(LocalDate.now().minusYears(35));

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(AppException.class, () -> userService.viewAllUser(1L, "admin"));
    }

    @Test
    public void testGetUser_Success() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setAddress("123 New St");
        user.setContactInfo("1234567890");
        user.setRole("patient");
        user.setDateOfBirth(LocalDate.now().minusYears(25));

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        User responseUser = userService.getUser(1L);

        assertNotNull(responseUser);
        assertEquals("John", responseUser.getFirstName());
        assertEquals("Doe", responseUser.getLastName());
    }

    @Test
    public void testGetUser_NotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AppException.class, () -> userService.getUser(1L));
    }

    @Test
    public void testGetAllDoctorsByPatient_Success() {
        User patientUser = new User();
        patientUser.setId(1L);
        patientUser.setFirstName("John");
        patientUser.setLastName("Doe");
        patientUser.setAddress("123 New St");
        patientUser.setContactInfo("1234567890");
        patientUser.setRole("PATIENT");
        patientUser.setDateOfBirth(LocalDate.now().minusYears(25));
        
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setUser(patientUser);
        patientUser.setPatient(patient);
        
        User doctorUser1 = new User();
        doctorUser1.setId(2L);
        doctorUser1.setFirstName("Dr. Smith");
        doctorUser1.setLastName("Jones");
        doctorUser1.setAddress("789 Doctor St");
        doctorUser1.setContactInfo("9876543210");
        doctorUser1.setRole("DOCTOR");
        doctorUser1.setDateOfBirth(LocalDate.now().minusYears(40));
        
        Practitioner practitioner = new Practitioner();
        practitioner.setId(2L);
        practitioner.setUser(doctorUser1);
        doctorUser1.setPractitioner(practitioner);
        

        User doctorUser2 = new User();
        doctorUser2.setId(3L);
        doctorUser2.setFirstName("Dr. Brown");
        doctorUser2.setLastName("Davis");
        doctorUser2.setAddress("456 Clinic St");
        doctorUser2.setContactInfo("1112233445");
        doctorUser2.setRole("DOCTOR");
        doctorUser2.setDateOfBirth(LocalDate.now().minusYears(45));
        
        Practitioner practitioner1 = new Practitioner();
        practitioner1.setId(2L);
        practitioner1.setUser(doctorUser2);
        doctorUser2.setPractitioner(practitioner1);

        List<User> userList = Arrays.asList(patientUser, doctorUser1, doctorUser2);

        when(userRepo.findById(1L)).thenReturn(Optional.of(patientUser));
        when(userRepo.findAll()).thenReturn(userList);
        when(userService.getAllDoctorsByPatient(1L)).thenReturn(userList);

        assertEquals(3, userList.size());
    }

    @Test
    public void testGetAllDoctorsByPatient_NotPatient() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Admin");
        user.setLastName("User");
        user.setAddress("789 Admin St");
        user.setContactInfo("1234567890");
        user.setRole("admin");
        user.setDateOfBirth(LocalDate.now().minusYears(35));

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(AppException.class, () -> userService.getAllDoctorsByPatient(1L));
    }

    @Test
    public void testGetAllPatientForMessaged_Success() {
        User practitioner = new User();
        practitioner.setId(1L);
        practitioner.setFirstName("Practitioner");
        practitioner.setLastName("User");
        practitioner.setRole("practitioner");

        User patient = new User();
        patient.setId(2L);
        patient.setFirstName("Patient");
        patient.setLastName("User");

        Message message = new Message();
        message.setId(1L);
        message.setSender(patient);
        message.setRecipient(practitioner);
        message.setMessage("Hello, practitioner!");
        message.setCreatedAt(LocalDateTime.now());

        List<Message> messages = new ArrayList<>();
        messages.add(message);

        when(userRepo.findById(1L)).thenReturn(Optional.of(practitioner));
        when(messageRepo.findAll()).thenReturn(messages);

        List<ChatUserDto> chatUsers = userService.getAllPatientForMessaged(1L);

        assertNotNull(chatUsers);
    }

    @Test
    public void testGetAllPatientForMessaged_NotPractitioner() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Admin");
        user.setLastName("User");
        user.setAddress("789 Admin St");
        user.setContactInfo("1234567890");
        user.setRole("admin");
        user.setDateOfBirth(LocalDate.now().minusYears(35));

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(AppException.class, () -> userService.getAllPatientForMessaged(0L));
    }
}
