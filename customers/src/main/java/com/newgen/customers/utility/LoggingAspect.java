package com.newgen.customers.utility;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.newgen.customers.exception.NewGenCustomersException;

import lombok.extern.log4j.Log4j2;

@Component
@Aspect
@Log4j2
public class LoggingAspect {
	@AfterThrowing(pointcut="execution(* com.newgen.customers.service.*Impl.*(..))", throwing="exception")
	public void logServiceExceptions(NewGenCustomersException exception) {
		log.error(exception.getMessage(), exception);
	}
}
