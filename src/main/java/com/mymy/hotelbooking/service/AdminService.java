package com.mymy.hotelbooking.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mymy.hotelbooking.model.Role;
import com.mymy.hotelbooking.model.User;
import com.mymy.hotelbooking.repository.RoleRepository;
import com.mymy.hotelbooking.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {	
	
	
	private final UserRepository userRepository;
	
	private final RoleRepository roleRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	public void createFirstAdmin()
	{
		if (!userRepository.existsByEmail("admin@gmail.com")) {
            boolean adminRoleExists = roleRepository.existsByName("ROLE_ADMIN");

            if (!adminRoleExists) {
                Role adminRole = new Role();
                adminRole.setName("ROLE_ADMIN");
                roleRepository.save(adminRole);
            }

            Optional<Role> adminRoleOptional = roleRepository.findByName("ROLE_ADMIN");
            if (adminRoleOptional.isPresent()) {
                Role adminRole = adminRoleOptional.get();

                User adminUser = new User();
                adminUser.setFirstName("Admin");
                adminUser.setLastName("Admin");
                adminUser.setEmail("admin@gmail.com");
                adminUser.setPassword(passwordEncoder.encode("admin123#"));
                adminUser.setRoles(Collections.singleton(adminRole));

                userRepository.save(adminUser);
            } else {
            	throw new RuntimeException("Failed to create admin user: ROLE_ADMIN not found");
            }  
		}
	}
}
