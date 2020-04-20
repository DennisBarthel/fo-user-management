package de.netos.domain;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RenewPasswordRequest {

	@NotBlank
	private String username;
	@NotBlank
	private String password;
	@NotBlank
	private String confirmPassword;
}
