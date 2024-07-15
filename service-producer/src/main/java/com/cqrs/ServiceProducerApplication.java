package com.cqrs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.integration.zookeeper.config.LeaderInitiatorFactoryBean;

@EnableDiscoveryClient
@SpringBootApplication
public class ServiceProducerApplication {
	@Autowired
	private LeaderInitiatorFactoryBean leaderInitiator;


	public static void main(String[] args) {
		SpringApplication.run(ServiceProducerApplication.class, args);
	}

}
