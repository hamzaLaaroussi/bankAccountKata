package com.devmaster.bankaccountkata.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@Builder
public class ApiExceptionPayload {

    private final String message;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;
}
