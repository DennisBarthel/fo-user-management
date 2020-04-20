package de.netos.service;

import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import de.netos.domain.RenewPasswordRequest;
import de.netos.domain.SignUpRequest;
import de.netos.exception.Error;
import de.netos.exception.RenewPasswordException;
import de.netos.exception.SignUpException;
import de.netos.model.User;
import de.netos.repository.UserRepository;

@Service
public class UserManagementService {
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Value("${password.expiry}")
    private int passwordExpiryOffset;

	public void signUp(SignUpRequest signUpRequest) throws SignUpException {
		if (repository.existsByUsername(signUpRequest.getEmail())) {
    		throw new SignUpException(Error.USERNAME_ALREADY_EXISTS, "Username already exists.");
    	}
    	
		if (!StringUtils.equals(signUpRequest.getPassword(), signUpRequest.getConfirmPassword())) {
			throw new SignUpException(Error.PASSWORD_CONFIRM_NOT_EQUAL, "Passwords aren't equal.");
		}
		
    	User user = new User();
    	user.setUsername(signUpRequest.getUsername());
    	user.setEmail(signUpRequest.getEmail());
    	user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
    	user.setFirstname(signUpRequest.getFirstname());
    	user.setLastname(signUpRequest.getLastname());
    	user.setCredentialsExpiryDate(LocalDate.now().plusDays(passwordExpiryOffset));
    	user.setAccountNonExpired(true);
    	user.setAccountNonLocked(true);
    	user.setCredentialsNonExpired(true);
    	user.setEnabled(true);
    	user.addAuthority(authorityService.getAuthorityByName("USER"));
    	
    	repository.save(user);
	}
	
	public void renewPassword(RenewPasswordRequest renewPasswordRequest) throws RenewPasswordException {
		if (!StringUtils.equals(renewPasswordRequest.getPassword(), renewPasswordRequest.getConfirmPassword())) {
			throw new RenewPasswordException(Error.PASSWORD_CONFIRM_NOT_EQUAL, "Passwords aren't equal");
		}
		
		User user = repository.findByUsername(renewPasswordRequest.getUsername())
				.orElseThrow(() -> new RenewPasswordException(Error.USERNAME_NOT_EXISTS, "User doesn't exist."));
		
		String newPassword = passwordEncoder.encode(renewPasswordRequest.getPassword());
		if (newPassword.equals(user.getPassword())) {
			throw new RenewPasswordException(Error.PASSWORD_NOT_CHANGED, "New password same as old one.");
		}
		
		user.setPassword(newPassword);
		user.setCredentialsExpiryDate(LocalDate.now().plusDays(passwordExpiryOffset));
		
		repository.save(user);
	}
}
