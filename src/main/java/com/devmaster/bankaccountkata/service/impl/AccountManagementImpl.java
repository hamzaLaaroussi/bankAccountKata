package com.devmaster.bankaccountkata.service.impl;

import com.devmaster.bankaccountkata.exception.BankOperationFailureException;
import com.devmaster.bankaccountkata.model.BankAccount;
import com.devmaster.bankaccountkata.service.AccountManagement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class AccountManagementImpl implements AccountManagement {


    @Autowired
    Map<String, BankAccount> accountsMap;

    @Override
    public BankAccount createBankAccount(BankAccount bankAccount) {
        accountsMap.put(bankAccount.getAccountNum(), bankAccount);
        return bankAccount;
    }

    @Override
    public BankAccount findAccountByClientId(long clientId) {
        return accountsMap.values()
                .stream()
                .filter(bankAccount -> bankAccount.getClient().getId() == clientId)
                .findFirst()
                .orElseThrow(() -> new BankOperationFailureException("No bank account found with the client id: " + clientId));
    }
}
