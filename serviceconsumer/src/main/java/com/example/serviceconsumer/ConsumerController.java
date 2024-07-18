package com.example.serviceconsumer;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
public class ConsumerController {

    private KafkaProducer<String, String> kafkaProducer;
    private KafkaConsumer<String, String> kafkaConsumer;
    private ObjectMapper mapper;
    private final Logger logger = getLogger(ConsumerController.class);

    public ConsumerController() {
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-1:29092,kafka-2:29093,kafka-3:29094");
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaProducer = new KafkaProducer<>(producerProps);
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-1:29092,kafka-2:29093,kafka-3:29094");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-group");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        kafkaConsumer = new KafkaConsumer<>(consumerProps);
    }


    @GetMapping("conference/token")
    public String token(Integer segment, String processorName, String tokenType, String owner)  {
        // Publish an event to topic-1
        Object[] arr = new Object[4];;
        arr[0] = new Integer(0);
        arr[1] = "processor1";
        arr[2] = "tokenType1";
        arr[3] = "owner1";
        String json;
        try {
            json = mapper.writeValueAsString(arr);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("topic-1", "key1", json);
        kafkaProducer.send(producerRecord);

        // TODO SEND TO PRODUCER

        try {
            Thread.sleep(Duration.ofNanos(500000));
            logger.info("SLEPT 0.5 MS");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        kafkaConsumer.subscribe(Collections.singleton("result-topic-1"));
        String result = "";
        int i = 0;
        while (true){
            ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofMillis(1));
            boolean test = consumerRecords==null;
            logger.info("Records is null: {}", test);
            logger.info("RECORD COUNT: {}" , String.valueOf(consumerRecords.count()));
            if (!consumerRecords.isEmpty()) {
                for (ConsumerRecord<String, String> record : consumerRecords) {
                    System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                    result = "offset =" + record.offset() + "key =" + record.key() + "value = " + record.value();
                    logger.info("offset ={}key ={}value = {}", record.offset(), record.key(), record.value());
                    // Process the result
                }
                i++;
                logger.info("Succeeded on iteration: {}", i);
                break;
            }
            i++;
            logger.info("Failed on iteration: {} " , i);
        }
        return result;
        // Return the result
    }
}