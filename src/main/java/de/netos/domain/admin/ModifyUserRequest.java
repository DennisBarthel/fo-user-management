package de.netos.domain.admin;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ModifyUserRequest {

	private LocalDate newPasswordExpiryDate;
}
