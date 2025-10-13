package com.corsairops.gateway.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic assetLocationUpdateTopic() {
        return TopicBuilder.name("asset-location-updates")
                .partitions(3)
                .replicas(1)
                .build();
    }
}