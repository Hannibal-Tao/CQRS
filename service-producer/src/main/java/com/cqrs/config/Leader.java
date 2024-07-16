package com.cqrs.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
    private final String hostname;
    private boolean isLeader = false;
    private Integer ID = 0;
    private boolean leaderDesignated = false;

    public Leader(CuratorFramework curatorClient) {
      String hostname1;
      this.curatorClient = curatorClient;
      try {
          hostname1 = InetAddress.getLocalHost().getHostName();
      } catch (UnknownHostException e) {
          hostname1 = "N/A";
      }
      this.hostname = hostname1;
    }


    @Override
    public String getRole() {
        return "leader";
    }

    @Override
    public String getId() {
      return this.hostname;
    }

    @Override
    public void onGranted(Context context) throws InterruptedException {
        // Handle the leadership granted event
        logger.info("Leadership granted for role: {}", getRole());
        leaderDesignated = true;
        becomeLeader();
    }

    @Override
    public void onRevoked(Context context) {
        // Handle the leadership revoked event
        logger.info("Leadership revoked for role: {}", getRole());
        isLeader = false;
        leaderDesignated = false;
        try {
            if(curatorClient.checkExists().watched().forPath("/services/service-producer-1/leader-node")!=null) {
                try {
                    curatorClient.delete().forPath("/services/service-producer-1/leader-node");
                    logger.info("Deleted leader-node from onRevoked()");
                } catch (Exception e) {
                    logger.error("Unable to delete leader{}", e.getMessage());
                }
            }
            else{
                logger.info("Path 'leader-node' not found");
            }
        } catch (Exception e) {
            logger.error("Unable to detect path{}", e.getMessage());
        }
        // Release resources, stop processes, etc.
    }

    private void becomeLeader() {
        try {
            // Create a leader node in Zookeeper
            curatorClient.create().forPath("/services/service-producer-1/leader-node",
                this.hostname.getBytes());
            isLeader = true;
            logger.info("Became the leader");
        } catch (KeeperException.NodeExistsException e) {
            // Node already exists, this instance is the leader
            isLeader = true;
            logger.error(
                "Became the leader (node already existed) client: {}, {}",
                e.getMessage(), curatorClient);

        } catch (Exception e) {
            logger.error("Error creating leader node in Zookeeper", e);
        }
    }
}