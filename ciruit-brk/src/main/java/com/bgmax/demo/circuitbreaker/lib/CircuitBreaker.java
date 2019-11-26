package com.bgmax.demo.circuitbreaker.lib;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

public abstract class CircuitBreaker {

	@Inject
	private Circuit circuit;

	private AtomicBoolean globaleConfig = new AtomicBoolean(false);
	 
	public Object around(final ProceedingJoinPoint pjp) throws Exception {

		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();

		
		Class<?> declaringClass = method.getDeclaringClass();
		ImmediateReturnWhen classConfiguration = declaringClass.getAnnotation(ImmediateReturnWhen.class);
		
		if (classConfiguration != null && globaleConfig.compareAndSet(false, true)) {
			circuit.setMaxFailures(classConfiguration.failuresThreshold());
			circuit.setFailureInterval(classConfiguration.failuresThresholdIntervalInSeconds());
			circuit.setSuccessThreshold(classConfiguration.successThreshold());
			
		}

		boolean executionOk = true;
		try {
			if (!circuit.checkIfClosedOrHalfOpen()) {
				return null;
			}
			return pjp.proceed();

		} catch (Throwable ex) {
			executionOk = false;
			circuit.newFailure();
			throw new Exception(ex);

		} finally {
			if(executionOk)
			this.circuit.newSuccess();
		}
	}

}