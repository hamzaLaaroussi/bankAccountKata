package com.devmaster.bankaccountkata.service;

import com.devmaster.bankaccountkata.exception.BankOperationFailureException;
import com.devmaster.bankaccountkata.model.BankAccount;
import com.devmaster.bankaccountkata.model.Operation;

import java.math.BigDecimal;
import java.util.List;


public interface AccountOperations {

    Operation deposit(BankAccount bankAccount, BigDecimal amount);

    Operation withdraw(BankAccount bankAccount, BigDecimal amount) throws BankOperationFailureException;

    List<Operation> checkHistory(BankAccount bankAccount);

}
