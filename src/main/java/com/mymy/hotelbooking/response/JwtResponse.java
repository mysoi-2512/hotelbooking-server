package com.mymy.hotelbooking.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
	
	private Long id;
	
	private String email;
	
	private String jwt;
	
	private String type = "Bearer";
	
	private List<String> roles;

	public JwtResponse(Long id, String email, String jwt, List<String> roles) {
		super();
		this.id = id;
		this.email = email;
		this.jwt = jwt;
		this.roles = roles;
	}	

}
