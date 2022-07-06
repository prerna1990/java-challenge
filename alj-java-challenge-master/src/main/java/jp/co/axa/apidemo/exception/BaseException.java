package jp.co.axa.apidemo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter

public class BaseException extends RuntimeException {


	private final HttpStatus httpStatus;
	private final String errorCode;

	public BaseException(String message, String errorCode) {
		super(message);
		this.errorCode = errorCode;
		this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
	}

	public BaseException(String message, HttpStatus httpStatus, String errorCode) {
		super(message);
		this.httpStatus = httpStatus;
		this.errorCode = errorCode;
	}
}

