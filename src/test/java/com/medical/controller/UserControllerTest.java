package com.medical.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.medical.dto.GetUserResponseDto;
import com.medical.dto.LoginDto;
import com.medical.dto.LoginResponseDto;
import com.medical.dto.UpdateUserDto;
import com.medical.dto.UserDto;
import com.medical.entity.User;
import com.medical.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserDto userDto;
    private LoginDto loginDto;
    private LoginResponseDto loginResponseDto;
    private UpdateUserDto updateUserDto;
    private User user;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setUsername("johnDoe");
        userDto.setPassword("Password123@");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setDateOfBirth(LocalDate.of(1990, 5, 1));
        userDto.setAddress("123 Street, City");
        userDto.setContactInfo("+1234567890");
        userDto.setRole("PATIENT");

        loginDto = new LoginDto();
        loginDto.setUsername("johnDoe");
        loginDto.setPassword("Password123@");

        loginResponseDto = new LoginResponseDto();
        loginResponseDto.setUsername("johnDoe");

        updateUserDto = new UpdateUserDto();
        updateUserDto.setFirstName("JohnUpdated");
        updateUserDto.setLastName("DoeUpdated");
        updateUserDto.setAddress("New Address");
        updateUserDto.setContactInfo("+9876543210");
        updateUserDto.setGetDateOfBirth(LocalDate.of(1991, 5, 1));

        user = new User();
        user.setId(1L);
        user.setUsername("johnDoe");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setAddress("123 Street, City");
        user.setContactInfo("+1234567890");
        user.setRole("PATIENT");
    }


    @Test
    void updateUser_ShouldReturnAcceptedStatus() {
        when(userService.updateUser(1L, updateUserDto)).thenReturn(user);

        ResponseEntity<User> response = userController.updateUser(1L, updateUserDto);

        assertAll(
            () -> assertEquals(HttpStatus.ACCEPTED, response.getStatusCode()),
            () -> assertEquals(user, response.getBody()),
            () -> verify(userService, times(1)).updateUser(1L, updateUserDto)
        );
    }

    @Test
    void updateUser_ShouldThrowRuntimeException_WhenServiceFails() {
        when(userService.updateUser(1L, updateUserDto)).thenThrow(new RuntimeException("Service failed"));

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> userController.updateUser(1L, updateUserDto)
        );

        assertEquals("Service failed", exception.getMessage());
    }

    @Test
    void getAllUser_ShouldReturnOkStatus() {
        when(userService.viewAllUser(0, null)).thenReturn(List.of(new GetUserResponseDto()));

        ResponseEntity<List<GetUserResponseDto>> response = userController.getAllUser(0, null);

        assertAll(
            () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
            () -> assertNotNull(response.getBody())
        );
    }

    @Test
    void getAllUser_ShouldReturnEmptyList_WhenNoUsers() {
        when(userService.viewAllUser(0, null)).thenReturn(List.of());

        ResponseEntity<List<GetUserResponseDto>> response = userController.getAllUser(0, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void getAllUser_ShouldThrowRuntimeException_WhenServiceFails() {
        when(userService.viewAllUser(0, null)).thenThrow(new RuntimeException("Service failed"));

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> userController.getAllUser(0, null)
        );

        assertEquals("Service failed", exception.getMessage());
    }

    @Test
    void getAllUser_ShouldReturnOk_WhenUsersExist() {
        when(userService.viewAllUser(0, "John")).thenReturn(List.of(new GetUserResponseDto()));

        ResponseEntity<List<GetUserResponseDto>> response = userController.getAllUser(0, "John");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getUser_ShouldReturnOkStatus() {
        when(userService.getUser(1L)).thenReturn(user);

        ResponseEntity<User> response = userController.getUser(1L);

        assertAll(
            () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
            () -> assertEquals(user, response.getBody())
        );
    }

    @Test
    void getUser_ShouldThrowRuntimeException_WhenServiceFails() {
        when(userService.getUser(1L)).thenThrow(new RuntimeException("Service failed"));

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> userController.getUser(1L)
        );

        assertEquals("Service failed", exception.getMessage());
    }

    @Test
    void getUser_ShouldReturnOk_WhenUserExists() {
        when(userService.getUser(1L)).thenReturn(user);

        ResponseEntity<User> response = userController.getUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
