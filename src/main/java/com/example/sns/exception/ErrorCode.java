package com.example.sns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    DUPLICATED_USER_NAME(HttpStatus.CONFLICT,"User name is duplicated"),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND , "User not founded"),

    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED , "Password is invalid"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED,"Token is invalid"),

    ;


    private HttpStatus status;
    private String messgage;
}
