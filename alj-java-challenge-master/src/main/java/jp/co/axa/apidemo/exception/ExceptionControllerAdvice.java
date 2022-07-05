package jp.co.axa.apidemo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
class ExceptionAdvice {

	@ExceptionHandler(EmployeeNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
		return new ResponseEntity<>(
			ExceptionResponse
				.builder()
				.status(HttpStatus.NOT_FOUND)
				.message(ex.getMessage())
				.build(),
			HttpStatus.NOT_FOUND
		);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleException(Exception ex) {
		log.error("handleException", ex);

		return new ResponseEntity<>(
			ExceptionResponse
				.builder()
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.message(ex.getMessage())
				.build(),
			HttpStatus.INTERNAL_SERVER_ERROR
		);

	}
}
