package com.devmaster.bankaccountkata.data;

import com.devmaster.bankaccountkata.model.BankAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@Slf4j
public class AccountListStoreConfiguration {

    private final Map<String, BankAccount> accountsMap = new ConcurrentHashMap<>();

    @Bean
    Map<String, BankAccount> getAccountsMap() {
        return this.accountsMap;
    }


}
