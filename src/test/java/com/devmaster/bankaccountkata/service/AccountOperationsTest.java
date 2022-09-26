package com.devmaster.bankaccountkata.service;

import com.devmaster.bankaccountkata.exception.BankOperationFailureException;
import com.devmaster.bankaccountkata.model.BankAccount;
import com.devmaster.bankaccountkata.model.Client;
import com.devmaster.bankaccountkata.model.Operation;
import com.devmaster.bankaccountkata.model.OperationType;
import com.devmaster.bankaccountkata.service.impl.AccountOperationsImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WebMvcTest(AccountOperationsImpl.class)
public class AccountOperationsTest {

    @InjectMocks
    private AccountOperationsImpl accountOperations;

    private static BankAccount emptyBankAccount;

    private static BankAccount bankAccountWithFunds;


    @BeforeAll
    public static void init() {

        Client client1 = Client.builder()
                .id(123456789)
                .firstName("Jon")
                .lastName("LEE")
                .build();
        emptyBankAccount = new BankAccount(); //Instantiate it for AccountNum and creationDate Initiation
        emptyBankAccount.setClient(client1);


        Client client2 = Client.builder()
                .id(111111111)
                .firstName("Janice")
                .lastName("green")
                .build();

        bankAccountWithFunds = new BankAccount();
        bankAccountWithFunds.setClient(client2);
        bankAccountWithFunds.setBalance(new BigDecimal("550.50"));

    }

    @Test
    void should_operationCreated_when_depositNewAmount() {

        BigDecimal initialBalance = emptyBankAccount.getBalance();
        BigDecimal operationAmount = new BigDecimal(1000);

        Operation operation = accountOperations.deposit(emptyBankAccount, operationAmount);

        assertEquals(OperationType.DEPOSIT, operation.getType());
        assertEquals(new BigDecimal(1000), operation.getAmount());
        assertEquals(initialBalance.add(operationAmount), operation.getBalance());

    }

    @Test
    void should_operationCreated_when_withdrawNewAmount() {

        BigDecimal initialBalance = bankAccountWithFunds.getBalance();
        BigDecimal operationAmount = new BigDecimal(50);

        Operation operation = accountOperations.withdraw(bankAccountWithFunds, operationAmount);

        assertEquals(OperationType.WITHDRAWAL, operation.getType());
        assertEquals(new BigDecimal(50), operation.getAmount());
        assertEquals(initialBalance.subtract(operationAmount), operation.getBalance());

    }

    @Test
    void should_throwsException_when_withdrawAmountIsBiggerThanBalance() {

        BigDecimal initialBalance = bankAccountWithFunds.getBalance();
        BigDecimal operationAmount = new BigDecimal(1000);


        BankOperationFailureException bankOperationFailureException = assertThrows(BankOperationFailureException.class, () -> {
            accountOperations.withdraw(bankAccountWithFunds, operationAmount);
        });

        assertEquals("Transaction cancelled due to insufficient funds", bankOperationFailureException.getMessage());

    }

    @Test
    void should_throwsException_when_withdrawFromNullAccount() {

        BigDecimal operationAmount = new BigDecimal(1000);


        BankOperationFailureException bankOperationFailureException = assertThrows(BankOperationFailureException.class, () -> {
            accountOperations.withdraw(null, operationAmount);
        });

        assertEquals("Bank account is null", bankOperationFailureException.getMessage());

    }

    @Test
    void should_throwsException_when_depositFromNullAccount() {

        BigDecimal operationAmount = new BigDecimal(1000);


        BankOperationFailureException bankOperationFailureException = assertThrows(BankOperationFailureException.class, () -> {
            accountOperations.deposit(null, operationAmount);
        });

        assertEquals("Bank account is null", bankOperationFailureException.getMessage());

    }

    @Test
    void should_throwsException_when_checkHistoryFromNullAccount() {

        BankOperationFailureException bankOperationFailureException = assertThrows(BankOperationFailureException.class, () -> {
            accountOperations.checkHistory(null);
        });

        assertEquals("Bank account is null", bankOperationFailureException.getMessage());

    }

    @Test
    void should_returnOperationHistory_when_operationsAdded() {

        BigDecimal operationAmount = new BigDecimal(50);

        accountOperations.withdraw(bankAccountWithFunds, operationAmount);
        accountOperations.deposit(bankAccountWithFunds, operationAmount);

        List<Operation> operationList = accountOperations.checkHistory(bankAccountWithFunds);

        assertEquals(2, operationList.size());
        assertEquals(OperationType.WITHDRAWAL, operationList.get(0).getType());
        assertEquals(OperationType.DEPOSIT, operationList.get(1).getType());

    }
}
