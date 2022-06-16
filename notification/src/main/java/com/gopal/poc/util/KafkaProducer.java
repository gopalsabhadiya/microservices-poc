package com.gopal.poc.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gopal.poc.dto.NotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final AppKafkaProperties appKafkaProperties;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(NotificationDTO notificationDTO){
        ObjectMapper ow = new ObjectMapper();

        String message = "{}";

        try {
            // convert user object to json string and return it
            message = ow.writeValueAsString(notificationDTO);
        }
        catch (JsonProcessingException e) {
            // catch various errors
            e.printStackTrace();
        }

        kafkaTemplate.send(appKafkaProperties.getTopic(), message);
    }
}
