package com.gopal.poc.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum UserTypeEnum {
    @JsonProperty("Regular")
    REGULAR("Regular"),
    @JsonProperty("System")
    SYSTEM("System");

    private final String userType;

    UserTypeEnum(String userType) {
        this.userType = userType;
    }


}
