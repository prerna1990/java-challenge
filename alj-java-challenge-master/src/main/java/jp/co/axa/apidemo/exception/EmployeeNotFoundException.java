package jp.co.axa.apidemo.exception;

public class EmployeeNotFoundException extends BaseException {

	public EmployeeNotFoundException(String message, String errorCode) {
		super(message, errorCode);
	}
}
