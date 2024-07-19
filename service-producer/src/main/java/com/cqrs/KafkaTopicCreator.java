package com.cqrs;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.KafkaFuture;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaTopicCreator {
    public static void createTopics(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "kafka-1:29092,kafka-2:29093,kafka-3:29094");

        Admin admin = Admin.create(props);

        String topic1name = "topic-1";
        String topic2name = "result-topic-1";

        int partitions = 1;
        short replicationFactor = 1;

        NewTopic topic1 = new NewTopic(topic1name,partitions,replicationFactor);
        NewTopic topic2 = new NewTopic(topic2name,partitions,replicationFactor);

        ArrayList<NewTopic> topicList = new ArrayList<NewTopic>();
        topicList.add(topic1);
        topicList.add(topic2);

        try {
            if(!(admin.listTopics().namesToListings().get().containsKey(topic1name))){
                CreateTopicsResult createTopicsResult = admin.createTopics(topicList);
                boolean topic1Created = createTopicsResult.values().containsKey(topic1name);
                System.out.println("Boolean test: " + topic1Created);
                System.out.println("Topic List: " + admin.listTopics().namesToListings().get().containsKey(topic1name));
                System.out.println("Topic: " + topic1name + " created successfully");
            }
            else{
                System.out.println("Topic already exists!");
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        // Close the AdminClient
        admin.close();
    }
}
