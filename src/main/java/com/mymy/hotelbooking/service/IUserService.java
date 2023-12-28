package com.mymy.hotelbooking.service;

import java.util.List;

import com.mymy.hotelbooking.model.User;

public interface IUserService {
	
	User registerUser(User user);
	
	List<User> getUsers();
	
	void deleteUser(String email);
	
	User getUser(String email);

	User registerAdmin(User user);

}
