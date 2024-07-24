package com.example.serviceconsumer;
import java.time.Duration;
import java.util.*;

import com.example.serviceconsumer.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.mongodb.client.MongoCollection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.bson.Document;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import static org.slf4j.LoggerFactory.getLogger;


@RestController
@RequestMapping("/consumer")
@Slf4j
@RequiredArgsConstructor
public class ConsumerController {

    private final ReplyingKafkaTemplate<String, String, String> template;
    @Autowired
    private MongoTemplate mongoTemplate;
    private MongoCollection<Document> collection;

    @Value(value = "${kafka.topic.command}")
    private String commandTopic;

    @Value(value = "${kafka.topic.query}")
    private String queryTopic;

    @Value(value = "${kafka.topic.result}")
    private String resultTopic;

    @PostMapping("command")
    public String command(String message) throws InterruptedException, ExecutionException, TimeoutException {
        if (!template.waitForAssignment(Duration.ofSeconds(10))) {
            throw new IllegalStateException("Reply container did not initialize");
        }
        Random random = new Random();
        int randLeaderPartition = random.nextInt(3);
        ProducerRecord<String, String> record = new ProducerRecord<>(commandTopic, randLeaderPartition,"key", message);
        RequestReplyFuture<String, String, String> replyFuture = template.sendAndReceive(record);
        SendResult<String, String> sendResult = replyFuture.getSendFuture().get(20, TimeUnit.SECONDS);
        log.info("Sent ok: {}", sendResult.getRecordMetadata());
        byte[] sendCorrelationID = sendResult.getProducerRecord().headers().lastHeader("kafka_correlationId").value();
        ConsumerRecord<String, String> consumerRecord = replyFuture.get(20, TimeUnit.SECONDS);
        byte[] recCorrelationID = consumerRecord.headers().lastHeader("kafka_correlationId").value();
        log.info("Template Prod Factory Listeners: {} ", template.getProducerFactory().getListeners());
        if(Arrays.equals(sendCorrelationID, recCorrelationID)){
            String result = consumerRecord.value();
            log.info("Return value: {}", consumerRecord.value());
            return result;
        }
        TimeUnit.SECONDS.sleep(20);
        return null;
    }


//    private KafkaProducer<String, String> kafkaProducer;
//    private KafkaConsumer<String, String> kafkaConsumer;
//    private ObjectMapper mapper;
//    private final Logger logger = getLogger(ConsumerController.class);
//
//    public ConsumerController() {
//        Properties producerProps = new Properties();
//        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-1:29092,kafka-2:29093,kafka-3:29094");
//        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        kafkaProducer = new KafkaProducer<>(producerProps);
//        mapper = new ObjectMapper();
//        mapper.enable(SerializationFeature.INDENT_OUTPUT);
//
//        Properties consumerProps = new Properties();
//        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-1:29092,kafka-2:29093,kafka-3:29094");
//        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group");
//        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        kafkaConsumer = new KafkaConsumer<>(consumerProps);
//    }
//
//
//    @GetMapping("conference/token")
//    public String token(Integer segment, String processorName, String tokenType, String owner)  {
//        // Publish an event to topic-1
//        Object[] arr = new Object[4];;
//        arr[0] = 0;
//        arr[1] = "processor1";
//        arr[2] = "tokenType1";
//        arr[3] = "owner1";
//        String json;
//        try {
//            json = mapper.writeValueAsString(arr);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("topic-1", "key1", json);
//        kafkaProducer.send(producerRecord);
//        // Wait for the result on result-topic-1 (not implemented yet)
//
//
//        // TODO SEND TO PRODUCER
//        kafkaConsumer.subscribe(Collections.singleton("result-topic-1"));
//        String result = "";
//        int i = 0;
//        while (true){
//            ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofNanos(500000));
//            boolean test = consumerRecords==null;
//            logger.info("Records is null: {}", test);
//            logger.info("RECORD COUNT: " + String.valueOf(consumerRecords.count()));
//            if (!consumerRecords.isEmpty()) {
//                for (ConsumerRecord<String, String> record : consumerRecords) {
//                    System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
//                    result = "offset =" + record.offset() + "key =" + record.key() + "value = " + record.value();
//                    logger.info("offset ={}key ={}value = {}", record.offset(), record.key(), record.value());
//                    // Process the result
//                }
//                i++;
//                logger.info("Succeeded on iteration: " + i);
//                break;
//            }
//            i++;
//            logger.info("Failed on iteration: " + i);
//        }
//        return result;
//        // Return the result
//        //wait for the result on the result-topic-1
//    }
}