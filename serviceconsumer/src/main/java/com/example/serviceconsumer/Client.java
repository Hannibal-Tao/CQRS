package com.example.serviceconsumer;

import com.example.serviceconsumer.entity.User;
import com.mongodb.client.*;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class Client {

    @Autowired
    private MongoTemplate mongoTemplate;
    private MongoCollection<Document> collection;



    public String produce(String message) {
        mongoTemplate.insert(User.builder().name(message).build());
        return message;
    }

}