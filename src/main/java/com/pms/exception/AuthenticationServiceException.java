package com.pms.exception;

public class AuthenticationServiceException extends Exception {
	private static final long serialVersionUID = 526706541554094375L;

	public AuthenticationServiceException(String message) {
		super(message);
	}

	public AuthenticationServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthenticationServiceException(Throwable cause) {
		super(cause);
	}

}
