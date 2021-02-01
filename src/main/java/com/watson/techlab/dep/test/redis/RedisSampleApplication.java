package com.watson.techlab.dep.test.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.watson.techlab.dep.redis","com.watson.techlab.dep.test.controller" })
public class RedisSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisSampleApplication.class, args);
	}
}
