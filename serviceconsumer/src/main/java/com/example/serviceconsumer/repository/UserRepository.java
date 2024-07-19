package com.example.serviceconsumer.repository;

import com.example.serviceconsumer.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    // 
}