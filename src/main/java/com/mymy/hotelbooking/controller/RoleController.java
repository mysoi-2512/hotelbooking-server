package com.mymy.hotelbooking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mymy.hotelbooking.exception.RoleAlReadyExistException;
import com.mymy.hotelbooking.model.Role;
import com.mymy.hotelbooking.model.User;
import com.mymy.hotelbooking.service.IRoleService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/roles")
public class RoleController {
	
	private final IRoleService roleService;
	
	@GetMapping("/all-roles")
	public List<String> getAllRoles() 
	{
		return roleService.getRoles();
	}
	
	@PostMapping("/create-new-role")
	public ResponseEntity<String> createRole(@RequestBody Role theRole)
	{
		try {
			roleService.createRole(theRole);
			return ResponseEntity.ok("New role created successfully!");
		} catch (RoleAlReadyExistException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
	}
	
	@DeleteMapping("/delete/{roleId}")
	public void deleteRole(@PathVariable("roleId") Long roleId) 
	{
		roleService.deleteRole(roleId);
	}
	
	@PostMapping("/remove-all-users-from-role/{roleId}")
	public Role removeAllUsersFromRole(@PathVariable("roleId") Long roleId)
	{
		return roleService.removeAllUsersFromRole(roleId);
	}
	
	@PostMapping("/remove-user-from-role")
	public User removeUserFromRole(@RequestParam("userId") Long userId,
									@RequestParam("roleId") Long roleId)
	{
		return roleService.removeUserFromRole(userId, roleId);
	}
	
	@PostMapping("/assign-user-to-role")
	public User assignRoleToUser(@RequestParam("userId") Long userId,
								@RequestParam("roleId") Long roleId)
	{
		return roleService.assignRoleToUser(userId, roleId);
	}

}
