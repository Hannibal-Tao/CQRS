package com.cqrs.config;

import jakarta.annotation.PreDestroy;
import javax.swing.JFileChooser;
import lombok.RequiredArgsConstructor;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.leader.DefaultCandidate;
import org.springframework.integration.zookeeper.config.LeaderInitiatorFactoryBean;
import org.springframework.stereotype.Component;

@Configuration
@RequiredArgsConstructor
public class ZooProducerConfig {

    private final Leader leader;

    private final CuratorFramework client;

    @Bean
    public LeaderInitiatorFactoryBean leaderInitiator() {
        return new LeaderInitiatorFactoryBean()
                    .setClient(client)
                    .setPath("/services/service-producer-1/")
                    .setCandidate(leader);
                    //.setRole("leader");
    }

    //@PreDestroy
    public void destroy() throws Exception {
        client.delete().forPath("/services/service-producer-1/leader-node");
        System.out.println(
                "Callback triggered - @PreDestroy.");
    }
}
