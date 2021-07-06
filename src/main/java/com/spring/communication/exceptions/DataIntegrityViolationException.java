package com.spring.communication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DataIntegrityViolationException extends RuntimeException{

	

	public DataIntegrityViolationException() {
		super();
	}
	public DataIntegrityViolationException(String exceptionMessage) {
		super(exceptionMessage);
	}
	public DataIntegrityViolationException(String exceptionMessage, Throwable cause) {
		super(exceptionMessage, cause);
	}

}
