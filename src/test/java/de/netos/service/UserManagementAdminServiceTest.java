package de.netos.service;

import static java.util.Optional.empty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.collect.Lists;

import de.netos.domain.admin.ModifyUserRequest;
import de.netos.domain.admin.UserResponse;
import de.netos.exception.Error;
import de.netos.exception.ModifyUserException;
import de.netos.model.User;
import de.netos.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserManagementAdminServiceTest {

	@InjectMocks
	private UserManagementAdminService objectUnderTest;
	
	@Mock
	private UserRepository repository;
	
	@Captor
	private ArgumentCaptor<User> userCaptor;
	
	@Test
	void loadAllUsers() {
		List<User> users = Lists.newArrayList(
				User.builder().username("A").build(),
				User.builder().username("B").build(),
				User.builder().username("C").build());
		when(repository.findAll()).thenReturn(users);
		
		List<UserResponse> allUsers = objectUnderTest.getAllUsers();
		
		assertThat(allUsers, hasSize(users.size()));
		assertThat(allUsers, containsInAnyOrder(
				UserResponse.builder().username("A").build(),
				UserResponse.builder().username("B").build(),
				UserResponse.builder().username("C").build()
				));
	}
	
	@Test
	void modifyUser_invalidPasswordExpiry() {
		ModifyUserRequest modifyUserRequest = ModifyUserRequest.builder()
			.newPasswordExpiryDate(LocalDate.now().minusDays(1))
			.build();
		
		ModifyUserException exception = assertThrows(ModifyUserException.class, () -> objectUnderTest.modifyUser("A", modifyUserRequest));
		
		assertThat(exception.getError(), is(Error.EXPIRY_DATE_INVALID));
	}
	
	@Test
	void modifyUser_unknownUser() {
		when(repository.findByUsername(anyString())).thenReturn(empty());
		
		LocalDate expiry = LocalDate.now().plusDays(7);
		ModifyUserRequest modifyUserRequest = ModifyUserRequest.builder()
				.newPasswordExpiryDate(LocalDate.now().plusDays(7))
				.build();
		
		ModifyUserException exception = assertThrows(ModifyUserException.class, () -> objectUnderTest.modifyUser("A", modifyUserRequest));
		
		assertThat(exception.getError(), is(Error.USERNAME_NOT_EXISTS));
	}
	
	@Test
	void modifyUser_success() throws ModifyUserException {
		when(repository.findByUsername(anyString())).thenReturn(Optional.of(new User()));
		
		LocalDate expiry = LocalDate.now().plusDays(7);
		ModifyUserRequest modifyUserRequest = ModifyUserRequest.builder()
				.newPasswordExpiryDate(expiry)
				.build();
		
		objectUnderTest.modifyUser("A", modifyUserRequest);
		
		verify(repository).save(userCaptor.capture());
		
		assertThat(expiry, is(userCaptor.getValue().getCredentialsExpiryDate()));
	}
}
