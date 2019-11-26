package com.bgmax.demo.controller;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.bgmax.demo.circuitbreaker.lib.CircuitBreaker;


@Component
@Aspect
public class GiftControllerAspect extends CircuitBreaker {
	
	
	/**
	 * Here we can control only sub set of methodes to be invoked
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around("execution(* com.bgmax.demo.controller..*.*(..))")
	public Object aroundServiceMethodAdvice(final ProceedingJoinPoint pjp)
	   throws Throwable {
	   
	   System.out.println(" Start aroundServiceMethodAdvice");
	   
	   return this.around(pjp);
	   
	}
}
