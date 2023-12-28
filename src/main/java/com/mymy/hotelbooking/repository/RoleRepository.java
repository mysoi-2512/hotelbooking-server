package com.mymy.hotelbooking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mymy.hotelbooking.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(String role);

	boolean existsByName(String roleName);

	@Query("SELECT DISTINCT r.name FROM Role r")
	List<String> getAllRoleName();

}
