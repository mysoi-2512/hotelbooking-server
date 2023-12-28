package com.mymy.hotelbooking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mymy.hotelbooking.exception.UserAlreadyExistsException;
import com.mymy.hotelbooking.model.User;
import com.mymy.hotelbooking.request.LoginRequest;
import com.mymy.hotelbooking.response.JwtResponse;
import com.mymy.hotelbooking.security.jwt.JwtUtils;
import com.mymy.hotelbooking.security.user.HotelUserDetails;
import com.mymy.hotelbooking.service.IUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final IUserService userService;
	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;
	
	@PostMapping("/register-user")
	public ResponseEntity<?> registerUser(@RequestBody User user) 
	{
		try {
			userService.registerUser(user);
			return ResponseEntity.ok("User Registration successful");
		} catch (UserAlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
	}
	
	@PostMapping("/register-admin")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> registerAdmin(@RequestBody User user) 
	{
		try {
			userService.registerAdmin(user);
			return ResponseEntity.ok("Admin Registration successful");
		} catch (UserAlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) 
	{
		Authentication authentication = 
				authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtTokenForUser(authentication);
		HotelUserDetails hotelUserDetails = (HotelUserDetails) authentication.getPrincipal();
		List<String> roles = hotelUserDetails.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.toList();
		
		return ResponseEntity.ok(new JwtResponse(
				hotelUserDetails.getId(),
				hotelUserDetails.getEmail(),
				jwt,
				roles));
	}

}
