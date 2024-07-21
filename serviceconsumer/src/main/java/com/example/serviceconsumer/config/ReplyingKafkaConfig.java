package com.example.serviceconsumer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

/**
 * https://docs.spring.io/spring-kafka/reference/kafka/sending-messages.html
 */
@Configuration
public class ReplyingKafkaConfig {
  @Value(value = "${kafka.topic.result}")
  private String resultTopic;

  @Value(value = "${kafka.topic.group}")
  private String groupId;

//  @Bean
//  public ApplicationRunner runner(ReplyingKafkaTemplate<String, String, String> template) {
//    return args -> {
//      if (!template.waitForAssignment(Duration.ofSeconds(10))) {
//        throw new IllegalStateException("Reply container did not initialize");
//      }
//      ProducerRecord<String, String> record = new ProducerRecord<>("kRequests", "foo");
//      RequestReplyFuture<String, String, String> replyFuture = template.sendAndReceive(record);
//      SendResult<String, String> sendResult = replyFuture.getSendFuture().get(10, TimeUnit.SECONDS);
//      System.out.println("Sent ok: " + sendResult.getRecordMetadata());
//      ConsumerRecord<String, String> consumerRecord = replyFuture.get(10, TimeUnit.SECONDS);
//      System.out.println("Return value: " + consumerRecord.value());
//    };
//  }

  @Bean
  public ReplyingKafkaTemplate<String, String, String> replyingTemplate(
      ProducerFactory<String, String> pf,
      ConcurrentMessageListenerContainer<String, String> repliesContainer) {

    return new ReplyingKafkaTemplate<>(pf, repliesContainer);
  }

  @Bean
  public ConcurrentMessageListenerContainer<String, String> repliesContainer(
      ConcurrentKafkaListenerContainerFactory<String, String> containerFactory) {

    ConcurrentMessageListenerContainer<String, String> repliesContainer =
        containerFactory.createContainer(resultTopic);
    repliesContainer.getContainerProperties().setGroupId(groupId);
    repliesContainer.setAutoStartup(false);
    return repliesContainer;
  }

}
