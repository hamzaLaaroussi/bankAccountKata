package com.devmaster.bankaccountkata.service.impl;

import com.devmaster.bankaccountkata.exception.BankOperationFailureException;
import com.devmaster.bankaccountkata.model.BankAccount;
import com.devmaster.bankaccountkata.model.Operation;
import com.devmaster.bankaccountkata.service.StatementPrinter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StatementPrinterImpl implements StatementPrinter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final String DASHED_LINE = "--------------------------------------------------------------------------------------------------------------------\n";

    @Override
    public String printAccountStatement(BankAccount bankAccount) {

        if (bankAccount == null) {
            throw new BankOperationFailureException("Bank account is null");
        }

        String header = printHeader();

        String operations = printOperations(bankAccount);

        String printStatement = DASHED_LINE + header + operations + DASHED_LINE;

        log.info("Account statement: {}", printStatement);

        return printStatement;
    }


    private String printHeader() {
        return String.join("|"
                , "date"
                , "operation"
                , "amount"
                , "balance"
                , "\n");
    }

    private String printOperations(BankAccount bankAccount) {
        return bankAccount
                .getOperations()
                .stream()
                .map(this::printOperation)
                .collect(Collectors.joining(""));
    }

    private String printOperation(Operation operation) {
        return String.join("|"
                , operation.getOperationDate().format(DATE_FORMATTER)
                , operation.getType().toString()
                , operation.getAmount().toString()
                , operation.getBalance().toString()
                , "\n");
    }
}
