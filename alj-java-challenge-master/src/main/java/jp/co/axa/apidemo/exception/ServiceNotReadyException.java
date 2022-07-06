package jp.co.axa.apidemo.exception;

import org.springframework.http.HttpStatus;

public class ServiceNotReadyException extends BaseException {

	public ServiceNotReadyException(String message,String errorCode) {
		super(message,HttpStatus.SERVICE_UNAVAILABLE, errorCode);
	}
}
