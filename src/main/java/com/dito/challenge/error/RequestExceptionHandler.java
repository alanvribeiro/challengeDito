package com.dito.challenge.error;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RequestExceptionHandler {

	@ExceptionHandler({ NoSuchElementException.class })
	public ResponseEntity<Object> notFoundError(Exception ex) {

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());

	}

	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Object> updateConstraintError(ConstraintViolationException ex) {

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body("Unfilled field(s): " + ex.getConstraintViolations().stream().map(
						constraint -> constraint.getPropertyPath().toString()).collect(Collectors.joining(", ")) + ".");
	}
	
	@ExceptionHandler({ BusinessRuleException.class })
	public ResponseEntity<Object> businessError(BusinessRuleException ex) {

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());

	}
	
}
