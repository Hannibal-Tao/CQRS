package com.cqrs.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaService {

  private final KafkaTemplate<String, String> template;

  @KafkaListener(id="commandListener", topics = "command-topic")
  public void commandListener(@Payload String in, @Header(KafkaHeaders.CORRELATION_ID) byte[] cid, @Header(KafkaHeaders.REPLY_TOPIC) String topic) {
    log.info("Command Listener received: payload {}, cid {}, topic {}", in, cid, topic);
    var record = new ProducerRecord<String, String>(topic, in.toUpperCase());
    record.headers().add(KafkaHeaders.CORRELATION_ID, cid);
    template.send(record);
  }

  @KafkaListener(id="queryListener", topics = "query-topic")
  @SendTo // use default replyTo expression
  public String queryListener(String in) {
    log.info("Query Listener received: {}", in);
    return in.toUpperCase();
  }

}
