package com.mymy.hotelbooking.service;

import java.util.List;

import com.mymy.hotelbooking.model.Role;
import com.mymy.hotelbooking.model.User;

public interface IRoleService {
	
	List<String> getRoles();
	
	Role createRole(Role theRole);
	
	void deleteRole(Long id);
	
	Role findByName(String name);
	
	User removeUserFromRole(Long userId, Long roleId);
	
	User assignRoleToUser(Long userId, Long roleId);
	
	Role removeAllUsersFromRole(Long roleId);

}
