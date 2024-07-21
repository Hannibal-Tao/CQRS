package com.cqrs.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

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


}
