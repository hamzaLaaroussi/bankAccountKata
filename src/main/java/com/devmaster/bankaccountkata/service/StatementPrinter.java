package com.devmaster.bankaccountkata.service;

import com.devmaster.bankaccountkata.model.BankAccount;

public interface StatementPrinter {

    String printAccountStatement(BankAccount bankAccount);

}
