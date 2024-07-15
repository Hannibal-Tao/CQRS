package com.cqrs.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.leader.Candidate;
import org.springframework.integration.leader.Context;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.*;

@Component
public class Leader implements Candidate {
    private final Logger logger = getLogger(Leader.class);
    private final CuratorFramework curatorClient;
    private boolean isLeader = false;

    public Leader(CuratorFramework curatorClient) {
        this.curatorClient = curatorClient;
    }


    @Override
    public String getRole() {
        return "leader";
    }

    @Override
    public String getId() {
        return "0";
    }

    @Override
    public void onGranted(Context context) throws InterruptedException {
        // Handle the leadership granted event
        logger.info("Leadership granted for role: {}", getRole());
        becomeLeader();
    }

    @Override
    public void onRevoked(Context context) {
        // Handle the leadership revoked event
        logger.info("Leadership revoked for role: {}", getRole());
        isLeader = false;
        // Release resources, stop processes, etc.
    }

    private void becomeLeader() {
        try {
            // Create a leader node in Zookeeper
            curatorClient.create().forPath("/services/service-producer-1/leader");
            isLeader = true;
            logger.info("Became the leader");
        } catch (KeeperException.NodeExistsException e) {
            // Node already exists, this instance is the leader
            isLeader = true;
            logger.info("Became the leader (node already existed)");
        } catch (Exception e) {
            logger.error("Error creating leader node in Zookeeper", e);
        }
    }
}