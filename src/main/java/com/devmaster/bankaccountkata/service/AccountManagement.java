package com.devmaster.bankaccountkata.service;

import com.devmaster.bankaccountkata.model.BankAccount;

public interface AccountManagement {
    BankAccount createBankAccount(BankAccount bankAccount);

    BankAccount findAccountByClientId(long clientId);
}
