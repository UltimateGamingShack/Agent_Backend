package com.newgen.policy.utility;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.newgen.policy.exception.NewgenPolicyException;

import lombok.extern.log4j.Log4j2;

@Component
@Aspect
@Log4j2
public class LoggingAspect {
	@AfterThrowing(pointcut="execution(* com.newgen.policy.service.*Impl.*(..))", throwing="exception")
	public void logServiceExceptions(NewgenPolicyException exception) {
		log.error(exception.getMessage(), exception);
	}
}
