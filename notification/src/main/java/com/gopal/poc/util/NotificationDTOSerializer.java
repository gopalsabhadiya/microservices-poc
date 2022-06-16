package com.gopal.poc.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gopal.poc.dto.NotificationDTO;
import org.apache.kafka.common.serialization.Deserializer;

public class NotificationDTOSerializer implements Deserializer<NotificationDTO> {
    @Override
    public NotificationDTO deserialize(String topic, byte[] data) {
        ObjectMapper objectMapper = new ObjectMapper();
        NotificationDTO notificationDTO = null;
        try {
            System.out.println("Trying to map byte array to object");
            notificationDTO = objectMapper.readValue(data, NotificationDTO.class);
            System.out.println("Your object: " + notificationDTO.toString());
        }
        catch (Exception e) {
            System.out.println("Error while deserializing Notification: " + data.toString());
            e.printStackTrace();
        }
        return notificationDTO;
    }
}
