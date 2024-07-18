/*package com.example.serviceconsumer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MessageIntegrationTest {


    @Autowired
    private static Client producer;

    @Autowired
    private static GreetingController consumer;

    @Test
    void testMessageFlow() {
        // Step 1: Send a message from the service producer
        String message = producer.produce();

        // Step 2: Retrieve the message from the service consumer
        String receivedMessage = consumer.greeting();

        // Step 3: Compare the messages from the producer and consumer
        assertEquals(message, receivedMessage);
    }
}*/