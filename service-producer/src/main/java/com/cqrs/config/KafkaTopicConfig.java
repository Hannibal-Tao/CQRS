package com.cqrs.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaTopicConfig {
  @Value(value = "${spring.kafka.bootstrap-servers}")
  private String bootstrapAddress;

  @Value(value = "${kafka.topic.command}")
  private String commandTopic;

  @Value(value = "${kafka.topic.query}")
  private String queryTopic;

  @Value(value = "${kafka.topic.result}")
  private String resultTopic;

  @Bean
  public KafkaAdmin kafkaAdmin() {
    Map<String, Object> configs = new HashMap<>();
    configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    return new KafkaAdmin(configs);
  }

  @Bean
  public NewTopic commandTopic() {
    return TopicBuilder.name(commandTopic)
        .partitions(10)
        .replicas(2)
        .build();
  }

  @Bean
  public NewTopic queryTopic() {
    return TopicBuilder.name(queryTopic)
        .partitions(10)
        .replicas(2)
        .build();
  }

  @Bean
  public NewTopic resultTopic() {
    return TopicBuilder.name(resultTopic)
        .partitions(10)
        .replicas(2)
        .build();
  }


//  @Bean
//  public ProducerFactory<String, String> producerFactory() {
//    Map<String, Object> configProps = new HashMap<>();
//    configProps.put(
//        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
//        bootstrapAddress);
//    configProps.put(
//        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
//        StringSerializer.class);
//    configProps.put(
//        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
//        StringSerializer.class);
//    return new DefaultKafkaProducerFactory<>(configProps);
//  }
//
//  @Bean
//  public KafkaTemplate<String, String> kafkaTemplate() {
//    return new KafkaTemplate<>(producerFactory());
//  }

}
