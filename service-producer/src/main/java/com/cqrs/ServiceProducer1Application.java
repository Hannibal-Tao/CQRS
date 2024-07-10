package com.cqrs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//@EnableDiscoveryClient
@SpringBootApplication
public class ServiceProducer1Application {

	public static void main(String[] args) {
		SpringApplication.run(ServiceProducer1Application.class, args);
	}

}
