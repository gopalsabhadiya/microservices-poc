package com.gopal.poc.dto;

import com.gopal.poc.util.UserTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {

    @Schema(hidden = true)
    private Long id;
    private String message;

    @Schema(hidden = true)
    private Boolean isRead;
    private Boolean hide;

    @Schema(hidden = true)
    private UserTypeEnum userType;

    private Long fromUser;
    private Long toUser;

    private Date createdAt;

}
