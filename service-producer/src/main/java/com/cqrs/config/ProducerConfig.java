package com.cqrs.config;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.leader.DefaultCandidate;
import org.springframework.integration.zookeeper.config.LeaderInitiatorFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class ProducerConfig {

    @Autowired
    private Leader leader;

    @Autowired
    private CuratorFramework client;

    @Bean
    public LeaderInitiatorFactoryBean leaderInitiator() {
        return new LeaderInitiatorFactoryBean()
                    .setClient(client)
                    .setPath("/services/service-producer-1/")
                    .setCandidate(leader);
                    //.setRole("leader");
}
}
