package com.gopal.poc.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.kafka")
@Data
public class AppKafkaProperties {
    private String topic;
    private String bootstrapServer;
    private String clientId;
    private String groupId;
}
