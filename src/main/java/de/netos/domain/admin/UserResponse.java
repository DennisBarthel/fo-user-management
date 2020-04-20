package de.netos.domain.admin;

import java.time.LocalDate;

import de.netos.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserResponse {

	private String username;
	private String email;
	private String firstname;
	private String lastname;
	private LocalDate passwordExpiryDate;
	
	public UserResponse(User user) {
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.firstname = user.getFirstname();
		this.lastname = user.getLastname();
		this.passwordExpiryDate = user.getCredentialsExpiryDate();
	}
}
