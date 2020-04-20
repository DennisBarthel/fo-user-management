package de.netos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.netos.domain.admin.ModifyUserRequest;
import de.netos.domain.admin.UserResponse;
import de.netos.exception.ModifyUserException;
import de.netos.service.UserManagementAdminService;

@RestController
@RequestMapping("/admin/user")
public class UserManagementAdminController {
	
	@Autowired
	private UserManagementAdminService service;

	@GetMapping("/")
	public List<UserResponse> getAllUsers() {
		return service.getAllUsers();
	}
	
	@DeleteMapping("/{username}")
	public void deleteUser(
			@PathVariable("username") String username) {
		service.deleteUser(username);
	}
	
	@PutMapping("/{userId}")
	public void modifyUser(
			@PathVariable("username") String username,
			@RequestBody ModifyUserRequest modifyUserRequest)
					throws ModifyUserException {
		service.modifyUser(username, modifyUserRequest);
	}
}
