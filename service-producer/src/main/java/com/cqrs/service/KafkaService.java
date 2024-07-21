package com.cqrs.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaService {

  @KafkaListener(id="commandListener", topics = "command-topic")
  @SendTo // use default replyTo expression
  public String commandListener(String in) {
    log.info("Command Listener received: {}", in);
    return in.toUpperCase();
  }

  @KafkaListener(id="queryListener", topics = "query-topic")
  @SendTo // use default replyTo expression
  public String queryListener(String in) {
    log.info("Query Listener received: {}", in);
    return in.toUpperCase();
  }

}
