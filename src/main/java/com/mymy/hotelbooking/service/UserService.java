package com.mymy.hotelbooking.service;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mymy.hotelbooking.exception.UserAlreadyExistsException;
import com.mymy.hotelbooking.model.Role;
import com.mymy.hotelbooking.model.User;
import com.mymy.hotelbooking.repository.RoleRepository;
import com.mymy.hotelbooking.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final RoleRepository roleRepository;

	@Override
	public User registerUser(User user) 
	{
		if (userRepository.existsByEmail(user.getEmail()))
		{
			throw new UserAlreadyExistsException(user.getEmail() + " already exists");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));		
		
		Role userRole = roleRepository.findByName("ROLE_USER").get();
		user.setRoles(Collections.singletonList(userRole));
		return userRepository.save(user);		
	}

	@Override
	public List<User> getUsers() 
	{
		return userRepository.findAll();
	}

	@Transactional
	@Override
	public void deleteUser(String email) 
	{
		User theUser = getUser(email);
		if(theUser != null) 
		{
			userRepository.deleteByEmail(email);
		}
		
	}

	@Override
	public User getUser(String email) 
	{
		return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

	@Override
	public User registerAdmin(User user) 
	{
		if (userRepository.existsByEmail(user.getEmail()))
		{
			throw new UserAlreadyExistsException(user.getEmail() + " already exists");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));		
		
		Role userRole = roleRepository.findByName("ROLE_ADMIN").get();
		user.setRoles(Collections.singletonList(userRole));
		return userRepository.save(user);	
	}

}
