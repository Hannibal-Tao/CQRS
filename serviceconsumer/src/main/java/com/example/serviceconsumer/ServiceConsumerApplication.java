package com.example.serviceconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableMongoRepositories(basePackages = "com.example.serviceconsumer.repository")
public class ServiceConsumerApplication {

	public static void main(String[] args) {

		SpringApplication.run(ServiceConsumerApplication.class, args);
	}

}
