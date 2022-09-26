package com.devmaster.bankaccountkata.exception;

public class BankOperationFailureException extends RuntimeException {
    public BankOperationFailureException(String message) {
        super(message);
    }
}
