package com.devmaster.bankaccountkata.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value= {BankOperationFailureException.class})
    public ResponseEntity<Object> handleApiRequestException(BankOperationFailureException bankOperationFailureException){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
         ApiExceptionPayload apiExceptionPayload = ApiExceptionPayload.builder()
                 .message(bankOperationFailureException.getMessage())
                 .httpStatus(badRequest)
                 .timestamp(ZonedDateTime.now())
                 .build();
         return new ResponseEntity<>(apiExceptionPayload, badRequest);
    }
}
