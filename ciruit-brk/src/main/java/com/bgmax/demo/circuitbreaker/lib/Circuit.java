package com.bgmax.demo.circuitbreaker.lib;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Circuit {

	private int successThreshold;
	
	private AtomicInteger successCounter = new AtomicInteger();
	
	private AtomicInteger failureCounter = new AtomicInteger();
	
	private int failuresThresholdInterval, failuresThreshold;

	private LocalTime closeTime, openTime;

	public void setSuccessThreshold(int successThreshold) {
		this.successThreshold = successThreshold;
	}

	@PostConstruct
	public void init() {
		this.closeTime = LocalTime.now();
		this.successThreshold = 10;
		this.successCounter.set(0);
		this.failureCounter.set(0);
	}

	public void newFailure() {
		this.failureCounter.incrementAndGet();
		openCircuitIfRequired();
	}

	private boolean isOpen() {
		return (this.closeTime == null) && (null != this.openTime);
	}

	private boolean isClosed() {
		return (this.closeTime != null) && (null == this.openTime);
	}

	public void newSuccess() {
		if (isClosed()) {
			return;
		}
		successCounter.incrementAndGet();
		// reactivate 
		if (successCounter.get() > successThreshold && isOpen()) {
			close();
		}
	}

	private void close() {
		
		if (isClosed()) return;
		
		synchronized (this) {
			
			if (isClosed()) return;
			
			this.openTime = null;
			this.failureCounter.set(0);
			this.closeTime = LocalTime.now();
			this.successCounter.set(0);
		}
	}

	/**
	 * Check if Circuit is Close Otherwise try to open It if time spend after last
	 * open time is greater then failure interval
	 * 
	 * @return
	 */
	public synchronized boolean checkIfClosedOrHalfOpen() {
		return (isClosed() || isHalfOpen());
	}

	private boolean isHalfOpen() {
		Long secondesBetween = ChronoUnit.SECONDS.between(this.openTime, LocalTime.now());
		return (secondesBetween % 3 == 0); // Circuit Half Open
	}
	
	private void open() {
		if (isOpen())
			return;
		synchronized (this) {
			if (isOpen())
				return;
			this.openTime = LocalTime.now();
			this.closeTime = null;
			this.failureCounter.set(0);
		}
	}

	private void openCircuitIfRequired() {
		if (this.failureCounter.get() >= this.failuresThreshold) {
			Long secondesBetween = ChronoUnit.SECONDS.between( this.closeTime, LocalTime.now());
			if (secondesBetween < failuresThresholdInterval) {
				open();
			}
		}
	}
	
	/**
	 * set Maximum failure before openin the circuit
	 * 
	 * @param maxFailures
	 */
	public void setMaxFailures(int maxFailures) {
		this.failuresThreshold = maxFailures;
	}

	/**
	 * set the failure interval in secondes all request such which are within [open
	 * time, open time + failure interval] will fail
	 * 
	 * @param failureInterval
	 */
	public void setFailureInterval(int failureInterval) {
		this.failuresThresholdInterval = failureInterval;
	}
}