package com.medical.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medical.dto.ChatUserDto;
import com.medical.dto.GetUserResponseDto;
import com.medical.dto.UpdateUserDto;
import com.medical.entity.User;
import com.medical.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin("*")
public class UserController {

	@Autowired
	UserService userService;

	@PutMapping("/{userId}")
	public ResponseEntity<User> updateUser(@Valid @PathVariable("userId") long userId,
			@RequestBody UpdateUserDto UpdateUserDto) {
		User user = userService.updateUser(userId, UpdateUserDto);
		return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
	}

	@GetMapping
	public ResponseEntity<List<GetUserResponseDto>> getAllUser(
			@RequestParam(name = "userId", defaultValue = "0", required = false) long userId,
			@RequestParam(name = "name", required = false) String name) {
		List<GetUserResponseDto> viewAllUser = userService.viewAllUser(userId, name);
		return new ResponseEntity<>(viewAllUser, HttpStatus.OK);
	}
	
	@GetMapping("/all-doctors")
	public ResponseEntity<List<User>> getAllDoctors() {
		List<User> viewAllUser = userService.getAllDoctors();
		return new ResponseEntity<>(viewAllUser, HttpStatus.OK);
	}

	@GetMapping("getuser/{userId}")
	public ResponseEntity<User> getUser(@PathVariable("userId") long userId) {
		User user = userService.getUser(userId);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	@PostMapping("/patients/{practitionerId}")
	public ResponseEntity<List<ChatUserDto>> getAllPatientForMessaged(@PathVariable("practitionerId") long practitionerId) {
		List<ChatUserDto> viewAllUser = userService.getAllPatientForMessaged(practitionerId);
		return new ResponseEntity<>(viewAllUser, HttpStatus.OK);
	}

}
