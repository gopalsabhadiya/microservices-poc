package com.gopal.poc.controller;

import com.gopal.poc.dto.NotificationDTO;
import com.gopal.poc.service.NotificationService;
import com.gopal.poc.util.exception.PageNumberPageSizeException;
import com.gopal.poc.util.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    private final KafkaReceiver<String, NotificationDTO> kafkaReceiver;

    /**
     * This HTTP method handles creating notification and sending them to live notification receiver once it's created
     * @param notificationDTO
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity createNotification(@RequestBody NotificationDTO notificationDTO) {
        log.info("Create Notification request received");
        try {
            return ResponseEntity.ok(notificationService.addNotification(notificationDTO));
        } catch (UserNotFoundException e) {
            log.error("UserNotFoundException: {}", e.getReason());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getReason());
        }
    }

    /**
     * This HTTP method handles all the notification fetching based on filters
     * @param id
     * @param fromUser
     * @param showHidden
     * @param pageNumber
     * @param pageSize
     * @return ResponseEntity
     */
    @GetMapping
    public ResponseEntity fetchNotifications(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Long fromUser,
            @RequestParam(required = false) Boolean showHidden,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize
    ) {

        log.info("Fetch Notification request received");
        List<NotificationDTO> notificationDTOList = null;
        try {
            notificationDTOList = notificationService.fetchNotificationList(id, fromUser, showHidden, pageNumber, pageSize);
        } catch (PageNumberPageSizeException e) {
            log.error("PageNumberPageSizeException: {}", e.getReason());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getReason());
        }

        return ResponseEntity.ok(notificationDTOList);

    }

    /**
     * this method handles Live notification client using Server Sent Events as Flux
     * @return Flux<Notification>
     */
    @GetMapping("/live")
    public Flux<NotificationDTO> liveNotifications() {

        log.info("Live Notification request received");
        Flux<ReceiverRecord<String, NotificationDTO>> kafkaFlux = kafkaReceiver.receive();
        return kafkaFlux.checkpoint("Messages are started being consumed").log()
                .doOnNext(
                        r -> r.receiverOffset().acknowledge()
                ).map(ReceiverRecord::value)
                .checkpoint("Messages are done consumed");
    }


}
