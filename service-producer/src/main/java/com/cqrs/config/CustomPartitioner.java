package com.cqrs.config;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CustomPartitioner implements Partitioner {
    private static final Logger logger = LoggerFactory.getLogger(CustomPartitioner.class);

    private final Leader leaderService;


    @Autowired
    public CustomPartitioner(Leader leaderService) {
        this.leaderService = leaderService;
    }

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int numPartitions = partitions.size();

        if (leaderService.isLeader()) {
            // Assign specific partitions to the leader node
            logger.info("Assigned leader to: {}", Utils.toPositive(Utils.murmur2(keyBytes)) % 3);
            logger.info("FOUND LEADER!");
            return Utils.toPositive(Utils.murmur2(keyBytes)) % 3;
        } else {
            // Use the default partitioning strategy for non-leader nodes
            logger.info("Assigned non-leader to:  {} " ,  Utils.toPositive(Utils.murmur2(keyBytes)) % numPartitions);
            return Utils.toPositive(Utils.murmur2(keyBytes)) % numPartitions;
        }
    }

    @Override
    public void close() {
        // No-op
    }

    @Override
    public void configure(Map<String, ?> configs) {
        // No-op
    }
}
