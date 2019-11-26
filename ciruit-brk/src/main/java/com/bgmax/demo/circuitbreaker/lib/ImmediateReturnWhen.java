package com.bgmax.demo.circuitbreaker.lib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ImmediateReturnWhen {

    public static final int FAILURES_THRESHOLD = 3;
    
    public static final int FAILURES_THRESHOLD_IN_SECONDS = 2;
    
    public static final int SUCCESS_THRESHOLD = 5;
    
  
    int failuresThreshold () default FAILURES_THRESHOLD;
   
    int failuresThresholdIntervalInSeconds () default FAILURES_THRESHOLD_IN_SECONDS;
    
    int successThreshold () default SUCCESS_THRESHOLD;
}