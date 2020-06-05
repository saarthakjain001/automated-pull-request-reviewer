package com.example.webhooksserver;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.webhooksserver.mapper.ObjectToPullRequestDetailDto;
import com.example.webhooksserver.mapper.ObjectToPushDetailDto;
import com.example.webhooksserver.ruleEngine.RuleEngine;

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