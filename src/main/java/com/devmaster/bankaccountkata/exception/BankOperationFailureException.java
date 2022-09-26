package com.devmaster.bankaccountkata.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
public class BankOperationFailureException extends RuntimeException {
    public BankOperationFailureException(String message) {
        super(message);
    }
}
