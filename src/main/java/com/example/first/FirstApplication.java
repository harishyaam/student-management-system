package com.example.first;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FirstApplication {

	private static final Logger logger =
			LoggerFactory.getLogger(FirstApplication.class);

	public static void main(String[] args) {
		logger.info("Student Management System is starting");
		SpringApplication.run(FirstApplication.class, args);
		logger.info("Student Management System has started");
	}}
