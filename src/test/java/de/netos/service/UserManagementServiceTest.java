package de.netos.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import de.netos.domain.RenewPasswordRequest;
import de.netos.domain.SignUpRequest;
import de.netos.exception.Error;
import de.netos.exception.RenewPasswordException;
import de.netos.exception.SignUpException;
import de.netos.model.Authority;
import de.netos.model.User;
import de.netos.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserManagementServiceTest {

	@InjectMocks
	private UserManagementService objectUnderTest;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private AuthorityService authorityService;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	private SignUpRequest signUpRequest;
	private RenewPasswordRequest renewPasswordRequest;
	
	@BeforeEach
	public void beforeEach() {
		signUpRequest = SignUpRequest.builder()
			.firstname("Erika")
			.lastname("Mustermann")
			.password("password")
			.confirmPassword("password")
			.email("e.m@test.com")
			.build();
		
		renewPasswordRequest = RenewPasswordRequest.builder()
			.username("user")
			.password("passwordNew")
			.confirmPassword("passwordNew")
			.build();
	}
	
	/*
	 * Given: User provides correct values
	 * When: Signing up
	 * Then: Sign up was successful
	 */
	@Test
	void addNewUser_Successful() throws SignUpException {
		when(authorityService.getAuthorityByName(anyString())).thenReturn(new Authority());
		when(userRepository.existsByUsername(anyString())).thenReturn(false);
		when(passwordEncoder.encode(any())).thenReturn("password");
		
		objectUnderTest.signUp(signUpRequest);
		
		verify(userRepository).existsByUsername(anyString());
		verify(userRepository).save(any(User.class));
		verifyNoMoreInteractions(userRepository);
	}
	
	/*
	 * Given: Password and confirm password aren't equal
	 * When: Signing up
	 * Then: Exception is thrown
	 */
	@Test
	void addNewUser_PasswordsNotEqual() {
		signUpRequest.setConfirmPassword("password1");
		
		when(userRepository.existsByUsername(anyString())).thenReturn(false);
		
		SignUpException exception = assertThrows(SignUpException.class, () -> objectUnderTest.signUp(signUpRequest));
		
		assertThat(exception.getError(), is(Error.PASSWORD_CONFIRM_NOT_EQUAL));
	}
	
	/*
	 * Given: User provides mail that is already used
	 * When: Signing up
	 * Then: Exception is thrown
	 */
	@Test
	void addNewUser_MailAlreadyUsed() {
		signUpRequest.setUsername("user");
		
		when(userRepository.existsByUsername(anyString())).thenReturn(true);
		
		SignUpException exception = assertThrows(SignUpException.class, () -> objectUnderTest.signUp(signUpRequest));
		
		assertThat(exception.getError(), is(Error.USERNAME_ALREADY_EXISTS));
	}
	
	/*
	 * Given: Correct re-new password request
	 * When: Change password
	 * Then: Password change was successful
	 */
	@Test
	void changePassword_Successful() throws RenewPasswordException {
		User userMock = Mockito.mock(User.class);
		when(userMock.getPassword()).thenReturn("password");
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userMock));
		when(passwordEncoder.encode(any())).thenReturn("passwordNew");
		
		objectUnderTest.renewPassword(renewPasswordRequest);
		
		verify(userRepository).findByUsername(anyString());
		verify(userRepository).save(any(User.class));
		verifyNoMoreInteractions(userRepository);
	}
	
	/*
	 * Given: User doesn't exist
	 * When: Change password
	 * Then: Exception is thrown
	 */
	@Test
	void changePassword_UserDoesNotExist() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
		
		RenewPasswordException exception = assertThrows(RenewPasswordException.class, () -> objectUnderTest.renewPassword(renewPasswordRequest));
		
		assertThat(exception.getError(), is(Error.USERNAME_NOT_EXISTS));
	}
	
	/*
	 * Given: Password hasn't changed
	 * When: Change password
	 * Then: Exception is thrown
	 */
	@Test
	void changePassword_PasswordHasNotChanged() {
		User userMock = Mockito.mock(User.class);
		when(userMock.getPassword()).thenReturn("password");
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userMock));
		when(passwordEncoder.encode(any())).thenReturn("password");
		
		RenewPasswordException exception = assertThrows(RenewPasswordException.class, () -> objectUnderTest.renewPassword(renewPasswordRequest));
		
		assertThat(exception.getError(), is(Error.PASSWORD_NOT_CHANGED));
	}
	
	/*
	 * Given: Password and confirm password are not equal
	 * When: Change password
	 * Then: Exception is thrown
	 */
	@Test
	void changePassword_PasswordsNotEqual() {
		renewPasswordRequest.setConfirmPassword("passwordNew1");
		
		RenewPasswordException exception = assertThrows(RenewPasswordException.class, () -> objectUnderTest.renewPassword(renewPasswordRequest));
		
		assertThat(exception.getError(), is(Error.PASSWORD_CONFIRM_NOT_EQUAL));
	}
}
