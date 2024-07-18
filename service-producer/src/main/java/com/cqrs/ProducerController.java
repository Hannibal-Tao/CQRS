package com.cqrs;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Collections;
import java.util.Properties;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@Slf4j
public class ProducerController {

    private final Logger logger = getLogger(ProducerController.class);

    @PostConstruct
    public void init(){
        // initialize thread so it can run continuously
        new Thread(() -> {
            // initialize kafka consumer
            Properties consumerProps = new Properties();
            consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-1:29092,kafka-2:29093,kafka-3:29094");
            consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "producer-group");
            consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

            KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(consumerProps);
            kafkaConsumer.subscribe(Collections.singleton("topic-1"));

            String key = "";
            String value = "";

            //read from topic-1 and save latest offset key and value
            while (true) {
                ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofNanos(500000));
                if (!consumerRecords.isEmpty()) {
                    for (ConsumerRecord<String, String> record : consumerRecords) {
                        logger.info("Consumed from topic-1: key={}, value={}", record.key(), record.value());
                        key = record.key();
                        value = record.value();
                    }
                    break;
                }
            }
            //initialize kafka producer
            Properties producerProps = new Properties();
            producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-1:29092,kafka-2:29093,kafka-3:29094");
            producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

            KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(producerProps);

            //create record
            ProducerRecord<String,String> producerRecord = new ProducerRecord<>("result-topic-1",key,value);

            //sends to topic
            kafkaProducer.send(producerRecord);
            logger.info("Published to result-topic-1: key={}, value={}", key, value);

        }).start();

    }

    @GetMapping("/produce")
    public String produce(){
        log.info("produce is called");
        return "Message from producer";
    }



}
