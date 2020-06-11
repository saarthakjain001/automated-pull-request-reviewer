package com.example.webhooksserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WebhooksServerApplication {

	public static void main(String[] args) {

		SpringApplication.run(WebhooksServerApplication.class, args);

	}
}