package com.gopal.poc.mappers;

import com.gopal.poc.dto.NotificationDTO;
import com.gopal.poc.entity.NotificationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    /**
     * This method maps Entity to DTO
     * @param notificationEntity
     * @return NotificationDTO
     */
    NotificationDTO mapNotificationEntityToDTO(NotificationEntity notificationEntity);

    /**
     * This method maps DTO to ENTITY
     * @param notificationDTO
     * @return NotificationEntity
     */
    NotificationEntity mapNotificationDTOToEntity(NotificationDTO notificationDTO);
}
