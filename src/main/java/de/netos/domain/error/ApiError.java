package de.netos.domain.error;

import de.netos.exception.Error;
import lombok.Getter;

@Getter
public class ApiError {

	private final String message;
    private final String error;
    private final String code;

    public ApiError(Error error, String message) {
    	this.code = error.getCode();
    	this.error = error.name();
    	this.message = message;
	}
}
