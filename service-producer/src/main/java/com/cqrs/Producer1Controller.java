package com.cqrs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Producer1Controller {
    @GetMapping("/produce")
    public String produce(){
        return "Message from producer 1";
    }

}
