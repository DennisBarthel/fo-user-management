package de.netos.exception;

import lombok.Getter;

public class RenewPasswordException extends Exception {

	@Getter
	private final Error error;
	
	public RenewPasswordException(Error error) {
		super();
		this.error = error;
	}
	
	public RenewPasswordException(Error error, String message) {
		super(message);
		this.error = error;
	}
}
