package jp.co.axa.apidemo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {

	@Getter
	private final HttpStatus httpStatus;

	@Getter
	private final String errorCode;

	public BaseException(String message, String errorCode) {
		super(message);
		this.errorCode = errorCode;
		this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
	}

	public BaseException(String message, Throwable cause, String errorCode) {
		super(message, cause);
		this.errorCode = errorCode;
		this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
	}

	public BaseException(String message, String errorCode, HttpStatus httpStatus) {
		super(message);
		this.errorCode = errorCode;
		this.httpStatus = httpStatus;
	}

	public BaseException(String message, Throwable cause, String errorCode, HttpStatus httpStatus) {
		super(message, cause);
		this.errorCode = errorCode;
		this.httpStatus = httpStatus;
	}
}

