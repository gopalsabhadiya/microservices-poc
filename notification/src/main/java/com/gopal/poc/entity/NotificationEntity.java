package com.gopal.poc.entity;

import com.gopal.poc.util.UserTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class NotificationEntity {

    @Id
    @SequenceGenerator(
            name="notification_id_sequence",
            sequenceName = "notification_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "notification_id_sequence"
    )
    private Long id;

    private String message;
    private Boolean isRead;
    private Boolean hide;

    private UserTypeEnum userType;

    private Long fromUser;
    private Long toUser;

    private Date createdAt;


}
