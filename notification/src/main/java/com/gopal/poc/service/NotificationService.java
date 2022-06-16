package com.gopal.poc.service;

import com.gopal.poc.clients.user.UserClientService;
import com.gopal.poc.util.ErrorMessageConstants;
import com.gopal.poc.util.KafkaProducer;
import com.gopal.poc.dto.NotificationDTO;
import com.gopal.poc.dto.UserDTO;
import com.gopal.poc.entity.NotificationEntity;
import com.gopal.poc.mappers.NotificationMapper;
import com.gopal.poc.repository.NotificationRepository;
import com.gopal.poc.util.exception.PageNumberPageSizeException;
import com.gopal.poc.util.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    private final UserClientService userClientService;

    private final KafkaProducer kafkaProducer;

    /**
     * This method adds notification in the DB and produces Kafka message
     * @param notificationDTO
     * @return NotificationDTO
     * @throws UserNotFoundException
     */
    public NotificationDTO addNotification(NotificationDTO notificationDTO) throws UserNotFoundException {

        log.info("Trying to send notification");
        notificationDTO.setIsRead(false);

        Boolean toUserExists = userClientService.userExists(notificationDTO.getToUser());
        Boolean fromUserExists = userClientService.userExists(notificationDTO.getFromUser());

        if(!toUserExists || !fromUserExists) {
            throw new UserNotFoundException(ErrorMessageConstants.TO_USER_FROM_USER_ERROR);
        }

        UserDTO userDTO = userClientService.fetchUserById(notificationDTO.getFromUser());

        notificationDTO.setUserType(userDTO.getUserType());

        NotificationEntity notificationEntity = notificationRepository.saveAndFlush(notificationMapper.mapNotificationDTOToEntity(notificationDTO));

        notificationDTO = notificationMapper.mapNotificationEntityToDTO(notificationEntity);

        log.info("Notification Sent Successfully: " + notificationDTO.toString());

        kafkaProducer.send(notificationDTO);

        return notificationDTO;
    }

    /**
     * This method handles fetching List<Notification> based on given filters and pagination request
     * @param id
     * @param fromUser
     * @param showHidden
     * @param pageNumber
     * @param pageSize
     * @return List<NotificationDTO>
     * @throws PageNumberPageSizeException
     */
    public List<NotificationDTO> fetchNotificationList(
            Long id, Long fromUser, Boolean showHidden, Integer pageNumber, Integer pageSize
    ) throws PageNumberPageSizeException {

        if ((pageNumber != null && pageSize == null) || (pageNumber == null && pageSize != null)) {
            throw new PageNumberPageSizeException(ErrorMessageConstants.PAGE_NUMBER_PAGE_SIZE_ERROR);
        }

        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setId(id);
        notificationEntity.setFromUser(fromUser);
        notificationEntity.setHide(showHidden);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withIgnoreNullValues();
        Example<NotificationEntity> notificationEntityExample = Example.of(notificationEntity, exampleMatcher);

        if (pageNumber != null) {
            Page<NotificationEntity> notificationEntityPage = notificationRepository.findAll(
                    notificationEntityExample,
                    PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"))
            );
            if(notificationEntityPage.hasContent()) {
                return notificationEntityPage.get()
                        .map(notificationMapper::mapNotificationEntityToDTO).collect(Collectors.toList());
            }
            return new ArrayList<>();
        }
        else {
            List<NotificationEntity> notificationEntityPage = notificationRepository.findAll(
                    notificationEntityExample,
                    Sort.by(Sort.Direction.DESC, "createdAt")
            );
                return notificationEntityPage.stream()
                        .map(notificationMapper::mapNotificationEntityToDTO).collect(Collectors.toList());
        }

    }
}
