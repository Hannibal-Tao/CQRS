package com.example.serviceconsumer;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/products")
public class GreetingController {
 
    @Autowired
    private Client helloWorldClient;

    @GetMapping("/get-greeting")
    public String greeting() {
        return helloWorldClient.produce();
    }

}