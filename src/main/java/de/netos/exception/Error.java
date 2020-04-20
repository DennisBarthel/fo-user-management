package de.netos.exception;

public enum Error {

	USERNAME_ALREADY_EXISTS("100"),
	USERNAME_NOT_EXISTS("200"),
	PASSWORD_NOT_CHANGED("201"),
	PASSWORD_CONFIRM_NOT_EQUAL("202"),
	EXPIRY_DATE_INVALID("1001"),
	INTERNAL_ERROR("9999");
	
	private final String code;
	
	private Error(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
}
