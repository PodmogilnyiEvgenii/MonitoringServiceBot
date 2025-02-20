package com.home.monitoringservicebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MonitoringServiceBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitoringServiceBotApplication.class, args);
	}

}
