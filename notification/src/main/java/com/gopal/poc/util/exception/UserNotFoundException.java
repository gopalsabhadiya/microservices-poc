package com.gopal.poc.util.exception;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Getter
public class UserNotFoundException extends Exception {
    private final String reason;
}
