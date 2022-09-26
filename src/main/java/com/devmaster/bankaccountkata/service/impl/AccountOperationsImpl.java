package com.devmaster.bankaccountkata.service.impl;

import com.devmaster.bankaccountkata.exception.BankOperationFailureException;
import com.devmaster.bankaccountkata.model.BankAccount;
import com.devmaster.bankaccountkata.model.Operation;
import com.devmaster.bankaccountkata.model.OperationType;
import com.devmaster.bankaccountkata.service.AccountOperations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class AccountOperationsImpl implements AccountOperations {

    private static final String NULL_ACCOUNT_MESSAGE = "Bank account is null";

    @Override
    public Operation deposit(BankAccount bankAccount, BigDecimal amount) {
        if (bankAccount == null) {
            throw new BankOperationFailureException(NULL_ACCOUNT_MESSAGE);
        }
        BigDecimal newBalance = bankAccount.getBalance().add(amount);

        Operation depositOperation = buildOperation(newBalance, amount, OperationType.DEPOSIT);

        bankAccount.getOperationList().add(depositOperation);
        bankAccount.setBalance(newBalance);

        return depositOperation;
    }


    @Override
    public Operation withdraw(BankAccount bankAccount, BigDecimal amount) {
        if (bankAccount == null) {
            throw new BankOperationFailureException(NULL_ACCOUNT_MESSAGE);
        }

        if (bankAccount.getBalance().compareTo(amount) < 0) {
            throw new BankOperationFailureException("Transaction cancelled due to insufficient funds");
        }

        BigDecimal newBalance = bankAccount.getBalance().subtract(amount);

        Operation withdrawOperation = buildOperation(newBalance, amount, OperationType.WITHDRAWAL);

        bankAccount.getOperationList().add(withdrawOperation);
        bankAccount.setBalance(newBalance);

        return withdrawOperation;
    }

    @Override
    public List<Operation> checkHistory(BankAccount bankAccount) {
        if (bankAccount == null) {
            throw new BankOperationFailureException(NULL_ACCOUNT_MESSAGE);
        }
        return bankAccount.getOperationList();
    }

    private Operation buildOperation(BigDecimal newBalance, BigDecimal amount, OperationType operationType) {
        return Operation.builder()
                .operationDate(LocalDate.now())
                .amount(amount)
                .type(operationType)
                .balance(newBalance)
                .build();
    }
}
