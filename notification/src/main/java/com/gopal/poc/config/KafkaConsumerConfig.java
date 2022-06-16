package com.gopal.poc.config;

import com.gopal.poc.dto.NotificationDTO;
import com.gopal.poc.util.AppKafkaProperties;
import com.gopal.poc.util.NotificationDTOSerializer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.internals.ConsumerFactory;
import reactor.kafka.receiver.internals.DefaultKafkaReceiver;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@AllArgsConstructor
public class KafkaConsumerConfig {

    private final AppKafkaProperties appKafkaProperties;

    /**
     * This method configures and creates KafkaReceiver for topic defined in application.properties
     * @return KafkaReceiver<String, NotificationDTO>
     */
    @Bean
    public KafkaReceiver<String, NotificationDTO> kafkaReceiver(){

        log.info("Initialising kafka receiver");

        new NewTopic("notification", 1, (short) 1);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, appKafkaProperties.getBootstrapServer());
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, appKafkaProperties.getClientId());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, appKafkaProperties.getGroupId());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, NotificationDTOSerializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);

        return new DefaultKafkaReceiver(ConsumerFactory.INSTANCE, ReceiverOptions.create(props).subscription(Collections.singleton(appKafkaProperties.getTopic())));
    }
}
