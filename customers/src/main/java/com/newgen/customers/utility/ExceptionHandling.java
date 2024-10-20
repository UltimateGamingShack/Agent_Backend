package com.newgen.customers.utility;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.newgen.customers.exception.NewGenCustomersException;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class ExceptionHandling {
	@Autowired
	Environment environment;
	
	@ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
	public ResponseEntity<ErrorInfo> generalExcetion(Exception e){
		ErrorInfo err = new ErrorInfo();
		err.setErrorCode(HttpStatus.BAD_REQUEST.value());
		err.setErrorMessage(e.getMessage());
		err.setTimestamp(LocalDateTime.now());
		
		return new ResponseEntity<ErrorInfo>(err, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorInfo> generalException(Exception e){
		ErrorInfo err = new ErrorInfo();
		err.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		err.setErrorMessage(environment.getProperty("General.MESSAGE")+ e.getMessage());
		err.setTimestamp(LocalDateTime.now());
		
		return new ResponseEntity<ErrorInfo>(err, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(NewGenCustomersException.class)
	public ResponseEntity<ErrorInfo> newgenPolicyExceptionHandler(NewGenCustomersException ngae){
		ErrorInfo err = new ErrorInfo();
		err.setErrorCode(HttpStatus.NOT_FOUND.value());
		err.setErrorMessage(environment.getProperty(ngae.getMessage()));
		err.setTimestamp(LocalDateTime.now());
		
		return new ResponseEntity<ErrorInfo>(err, HttpStatus.NOT_FOUND);
	}
}
