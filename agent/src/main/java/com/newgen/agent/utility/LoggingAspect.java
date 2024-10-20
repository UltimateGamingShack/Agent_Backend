package com.newgen.agent.utility;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.newgen.agent.exception.NewGenAgentException;

import lombok.extern.log4j.Log4j2;

@Component
@Aspect
@Log4j2
public class LoggingAspect {
	@AfterThrowing(pointcut="execution(* com.newgen.agent.service.*Impl.*(..))", throwing="exception")
	public void logServiceExceptions(NewGenAgentException exception) {
		log.error(exception.getMessage(), exception);
	}
}
