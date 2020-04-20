package de.netos.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.netos.domain.admin.ModifyUserRequest;
import de.netos.domain.admin.UserResponse;
import de.netos.exception.Error;
import de.netos.exception.ModifyUserException;
import de.netos.model.User;
import de.netos.repository.UserRepository;

@Service
public class UserManagementAdminService {

	@Autowired
	private UserRepository repository; 
	
	public List<UserResponse> getAllUsers() {
		List<User> allUsers = repository.findAll();
		
		return allUsers.stream()
				.map(UserResponse::new)
				.collect(Collectors.toList());
	}
	
	@Transactional
	public void deleteUser(String username) {
		repository.deleteByUsername(username);
	}

	public void modifyUser(String username, ModifyUserRequest modifyUserRequest) throws ModifyUserException {
		LocalDate today = LocalDate.now();
		if (today.isAfter(modifyUserRequest.getNewPasswordExpiryDate())) {
			throw new ModifyUserException(Error.EXPIRY_DATE_INVALID, "New expiry can't be before current date");
		}
		
		User user = repository.findByUsername(username)
			.orElseThrow(() -> new ModifyUserException(Error.USERNAME_NOT_EXISTS, "User doesn't exist."));
		
		user.setCredentialsExpiryDate(modifyUserRequest.getNewPasswordExpiryDate());
		
		repository.save(user);
	}
}
