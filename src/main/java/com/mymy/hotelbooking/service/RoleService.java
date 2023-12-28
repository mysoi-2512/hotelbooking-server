package com.mymy.hotelbooking.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mymy.hotelbooking.exception.RoleAlReadyExistException;
import com.mymy.hotelbooking.exception.UserAlreadyExistsException;
import com.mymy.hotelbooking.model.Role;
import com.mymy.hotelbooking.model.User;
import com.mymy.hotelbooking.repository.RoleRepository;
import com.mymy.hotelbooking.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
	
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;

	@Override
	public List<String> getRoles() {
		return roleRepository.getAllRoleName();
	}

	@Override
	public Role createRole(Role theRole) {
		String roleName = "ROLE_" + theRole.getName().toUpperCase();
		Role role = new Role(roleName);
		if(roleRepository.existsByName(roleName))
		{
			throw new RoleAlReadyExistException(theRole.getName() + " role already exists");
		}
		return roleRepository.save(role);
	}

	@Override
	public void deleteRole(Long roleId) {
		this.removeAllUsersFromRole(roleId);
		roleRepository.deleteById(roleId);		
	}

	@Override
	public Role findByName(String name) {
		return roleRepository.findByName(name).get();
	}

	@Override
	public User removeUserFromRole(Long userId, Long roleId) {
		Optional<User> user = userRepository.findById(userId);
		Optional<Role> role = roleRepository.findById(roleId);
		if (role.isPresent() && role.get().getUsers().contains(user.get()))
		{
			role.get().removeUserFromRole(user.get());
			roleRepository.save(role.get());
			return user.get();
		}
		throw new UsernameNotFoundException("User not found");
	}

	@Override
	public User assignRoleToUser(Long userId, Long roleId) {
		Optional<User> user = userRepository.findById(userId);
		Optional<Role> role = roleRepository.findById(roleId);
		if(user.isPresent() && user.get().getRoles().contains(role.get()))
		{
			throw new UserAlreadyExistsException(user.get().getFirstName() + " is already assigned to the " + role.get().getName());
		}
		if (role.isPresent())
		{
			role.get().assignRoleToUser(user.get());
			roleRepository.save(role.get());
			return user.get();
		}
		return null;
	}

	@Override
	public Role removeAllUsersFromRole(Long roleId) {
		Optional<Role> role = roleRepository.findById(roleId);
		role.get().removeAllUserFromRole();
		return roleRepository.save(role.get());
	}

}
