package de.netos.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import de.netos.domain.error.ApiError;
import de.netos.exception.Error;
import de.netos.exception.ModifyUserException;
import de.netos.exception.RenewPasswordException;
import de.netos.exception.SignUpException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({
		RenewPasswordException.class,
		SignUpException.class,
		ModifyUserException.class
	})
	public ResponseEntity<ApiError> handleCustomException(RenewPasswordException exception) {
		log.error(exception.getMessage(), exception);
		
		ApiError apiError = new ApiError(exception.getError(), exception.getMessage());
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleUnhandled(Exception exception) {
		log.error(exception.getMessage(), exception);
		
		ApiError apiError = new ApiError(Error.INTERNAL_ERROR, "Internal error occured");
		return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
