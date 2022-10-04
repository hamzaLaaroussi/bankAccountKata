package com.devmaster.bankaccountkata.service;

import com.devmaster.bankaccountkata.exception.BankOperationFailureException;
import com.devmaster.bankaccountkata.model.BankAccount;
import com.devmaster.bankaccountkata.model.Client;
import com.devmaster.bankaccountkata.model.Operation;
import com.devmaster.bankaccountkata.model.OperationType;
import com.devmaster.bankaccountkata.service.impl.StatementPrinterImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(StatementPrinterImpl.class)
public class StatementPrinterTest {

    @InjectMocks
    StatementPrinterImpl statementPrinter;

    private static BankAccount bankAccountWithOperations;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    @BeforeAll
    public static void init() {

        Client client = Client.builder()
                .id(111111111)
                .firstName("Janice")
                .lastName("green")
                .build();

        Operation operationDeposit = Operation.builder()
                .operationDate(LocalDate.of(2022, 9, 22))
                .amount(new BigDecimal(100))
                .balance(new BigDecimal(1100))
                .type(OperationType.DEPOSIT)
                .build();

        Operation operationWithdraw = Operation.builder()
                .operationDate(LocalDate.of(2022, 9, 12))
                .amount(new BigDecimal(50))
                .balance(new BigDecimal(1050))
                .type(OperationType.WITHDRAWAL)
                .build();

        bankAccountWithOperations = new BankAccount();
        bankAccountWithOperations.setClient(client);
        bankAccountWithOperations.setBalance(new BigDecimal("550.50"));
        bankAccountWithOperations.setOperations(Arrays.asList(operationDeposit, operationWithdraw));

    }

    @Test
    void should_throwError_when_bankAccountIsNull() {
        BankOperationFailureException bankOperationFailureException = assertThrows(BankOperationFailureException.class, () -> {
            statementPrinter.printAccountStatement(null);
        });
        assertEquals("Bank account is null", bankOperationFailureException.getMessage());
    }

    @Test
    void should_printStatement_when_bankAccountHavingOperations() {
        String statementPrint = statementPrinter.printAccountStatement(bankAccountWithOperations);
        assertNotNull(statementPrint);
        Operation depositOperation = bankAccountWithOperations.getOperations().get(0);
        Operation withdrawOperation = bankAccountWithOperations.getOperations().get(1);
        final String headerPrint = "date|operation|amount|balance|";
        final String depositOperationPrint = "" + depositOperation.getOperationDate().format(DATE_FORMATTER)
                + "|"
                + depositOperation.getType()
                + "|"
                + depositOperation.getAmount()
                + "|"
                + depositOperation.getBalance()
                + "|";
        final String withdrawOperationPrint = "" + withdrawOperation.getOperationDate().format(DATE_FORMATTER)
                + "|"
                + withdrawOperation.getType()
                + "|"
                + withdrawOperation.getAmount()
                + "|"
                + withdrawOperation.getBalance()
                + "|";

        assertTrue(statementPrint.contains(headerPrint));
        assertTrue(statementPrint.contains(depositOperationPrint));
        assertTrue(statementPrint.contains(withdrawOperationPrint));


    }


}
