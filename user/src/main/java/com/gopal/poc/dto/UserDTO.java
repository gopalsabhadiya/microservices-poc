package com.gopal.poc.dto;

import com.gopal.poc.util.UserTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @Schema(hidden = true)
    private Long id;
    private String userName;

    @Schema(type = "string", allowableValues = {"Regular", "System"})
    private UserTypeEnum userType;
}
