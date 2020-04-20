package de.netos.exception;

import lombok.Getter;

public class ModifyUserException extends Exception {

	@Getter
	private final Error error;
	
	public ModifyUserException(Error error) {
		super();
		this.error = error;
	}
	
	public ModifyUserException(Error error, String message, Throwable cause) {
		super(message, cause);
		this.error = error;
	}
	
	public ModifyUserException(Error error, String message) {
		super(message);
		this.error = error;
	}
}
