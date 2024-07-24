package com.cqrs.service;


import com.cqrs.entity.User;
import com.mongodb.client.MongoCollection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartitionInfo;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import com.cqrs.config.Leader;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaService {

  private final KafkaTemplate<String, String> template;
  private final Leader leader;
  @Autowired
  private MongoTemplate mongoTemplate;
  private MongoCollection<Document> collection;

  @KafkaListener(id="commandListener", topics = "command-topic")
  public void commandListener(@Payload String in, @Header(KafkaHeaders.CORRELATION_ID) byte[] cid, @Header(KafkaHeaders.REPLY_TOPIC) String topic) {
    // check num partitions
    // KafkaAdmin admin = template.getKafkaAdmin();
    //int leaderPartition = getLeaderPartition(topic);
    // log.info("LEADER PARTITION: " + leaderPartition);
    log.info("Command Listener received: payload {}, cid {}, topic {}", in, cid, topic);
    var record = new ProducerRecord<String, String>(topic, in.toUpperCase());
    record.headers().add(KafkaHeaders.CORRELATION_ID, cid);
    log.info("Template Prod Factory Listeners: {} ", template.getProducerFactory().getListeners());
    mongoTemplate.insert(User.builder().name(in.toUpperCase()).build());
    template.send(record);
  }

  /*private int getLeaderPartition(String topic){
      Properties props = new Properties();
      props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-1:19092,kafka-2:19093,kafka-3:19094");
      AdminClient adminClient = AdminClient.create(props);
      DescribeTopicsResult result = adminClient.describeTopics(Collections.singletonList(topic));
      TopicDescription topicDescription;
      try {
          topicDescription = result.allTopicNames().get().get(topic);
      } catch (InterruptedException | ExecutionException e) {
          throw new RuntimeException(e);
      }
      for(TopicPartitionInfo partitionInfo : topicDescription.partitions()){
          Node leader = partitionInfo.leader();
          System.out.println("ON PARTITION: " + partitionInfo.partition());
          if(leader!=null){
              return partitionInfo.partition();
          }
      }
      return 0;
  }*/

  @KafkaListener(id="queryListener", topics = "query-topic")
  @SendTo // use default replyTo expression
  public String queryListener(String in) {
    log.info("Query Listener received: {}", in);
    return in.toUpperCase();
  }

}
