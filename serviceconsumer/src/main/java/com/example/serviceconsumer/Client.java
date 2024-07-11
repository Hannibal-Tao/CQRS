package com.example.serviceconsumer;

import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Configuration
@EnableFeignClients
@EnableDiscoveryClient
public class Client {
 
    @Autowired
    private TheClient theClient;

    @FeignClient(name = "service-producer")
    interface TheClient {
 
        @RequestMapping(path = "/produce", method = RequestMethod.GET)
        @ResponseBody
	    String helloWorld();
    }
    public String produce() {
        return theClient.helloWorld();
    }
}