package com.cqrs.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.support.SimpleKafkaHeaderMapper;
import org.springframework.kafka.support.converter.MessagingMessageConverter;

@EnableKafka
@Configuration
@Slf4j
public class KafkaListenerConfig {

  @Bean // not required if Jackson is on the classpath
  public MessagingMessageConverter simpleMapperConverter() {
    MessagingMessageConverter messagingMessageConverter = new MessagingMessageConverter();
    messagingMessageConverter.setHeaderMapper(new SimpleKafkaHeaderMapper());
    return messagingMessageConverter;
  }
}
