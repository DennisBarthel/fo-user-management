package de.netos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.netos.domain.RenewPasswordRequest;
import de.netos.domain.SignUpRequest;
import de.netos.exception.RenewPasswordException;
import de.netos.exception.SignUpException;
import de.netos.service.UserManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/user")
@Api(value = "Client-facing User Management service")
public class UserManagementController {
	
	@Autowired
	private UserManagementService service;
	
	@ApiOperation(value = "Creates a new user in the system.")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "User was successfully created"),
			@ApiResponse(code = 400, message = "There")
	})
	@PostMapping("/sign_up")
	public ResponseEntity<String> signUp(
			@RequestBody SignUpRequest signUpRequest) 
					throws SignUpException {
		service.signUp(signUpRequest);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "Sets a new password for an existing user")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Password successfully changed"),
			@ApiResponse(code = 400, message = "Request was invalid")
	})
	@PostMapping("/renew_password")
	public ResponseEntity<String> renewPassword(
			@RequestBody RenewPasswordRequest renewPasswordRequest) 
					throws RenewPasswordException {
		service.renewPassword(renewPasswordRequest);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
