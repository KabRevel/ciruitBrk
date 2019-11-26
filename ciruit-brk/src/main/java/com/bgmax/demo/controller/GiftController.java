package com.bgmax.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bgmax.demo.circuitbreaker.lib.ImmediateReturnWhen;
import com.bgmax.demo.model.Gift;

@RestController
@ImmediateReturnWhen(failuresThreshold = 5, failuresThresholdIntervalInSeconds = 10, successThreshold = 10)
public class GiftController {

	@RequestMapping("/")
	public List<Gift> getEmployees() {
		List<Gift> gitList = new ArrayList<Gift>();
		gitList.add(new Gift(50, "gif1"));
		return gitList;
	}

	@RequestMapping("/advice")
	public List<Gift> getEmployeesAdvice() {
		List<Gift> gitList = new ArrayList<Gift>();
		gitList.add(new Gift(50, "gif1"));
		return gitList;
	}
}
