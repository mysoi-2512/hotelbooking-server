package com.mymy.hotelbooking.controller;

import java.util.ArrayList;
import java.util.List;

//import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mymy.hotelbooking.model.User;
import com.mymy.hotelbooking.response.ProfileResponse;
import com.mymy.hotelbooking.service.IUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	
	private final IUserService userService;
	
	@GetMapping("/all")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<ProfileResponse>> getUsers()
	{
		List<User> users = userService.getUsers();
		List<ProfileResponse> profileResponses = new ArrayList<>();
		for (User user : users) {
			ProfileResponse response = getProfileResponse(user);
			profileResponses.add(response);
		}
		return ResponseEntity.ok(profileResponses);
	}
	
	@GetMapping("/{email}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and #email == principal.username)")
	public ResponseEntity<?> getUserByEmail(@PathVariable("email") String email) 
	{
		try {
			User theUser = userService.getUser(email);		
			ProfileResponse response = getProfileResponse(theUser);
			return ResponseEntity.ok(response);
		} catch (UsernameNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user");
		}
	}
	
	@DeleteMapping("/delete/{userId}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and #email == principal.username)")
	public ResponseEntity<String> deleteUser(@PathVariable("userId") String email)
	{
		try {
			userService.deleteUser(email);
			return ResponseEntity.ok("User delete successfully");
		} catch (UsernameNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user");
		}
	}	

	public ProfileResponse getProfileResponse(User user) {		
		return new ProfileResponse(
				user.getId(), 
				user.getFirstName(), 
				user.getLastName(), 
				user.getEmail());
	}	
	
}
