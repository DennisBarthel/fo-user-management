package de.netos.exception;

import lombok.Getter;

public class SignUpException extends Exception {

	@Getter
	private final Error error;

	public SignUpException(Error error) {
		super();
		this.error = error;
	}

	public SignUpException(Error error, String message, Throwable cause) {
		super(message, cause);
		this.error = error;
	}

	public SignUpException(Error error, String message) {
		super(message);
		this.error = error;
	}

	public SignUpException(Error error, Throwable cause) {
		super(cause);
		this.error = error;
	}
}
