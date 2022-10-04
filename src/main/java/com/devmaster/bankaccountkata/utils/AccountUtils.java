package com.devmaster.bankaccountkata.utils;

import java.util.Random;

public class AccountUtils {

    private AccountUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String generateBankAccount(){
        Random rand = new Random();

        StringBuilder accountNumber = new StringBuilder("FR");
        for (int i = 0; i < 14; i++)
        {
            int n = rand.nextInt(10);
            accountNumber.append(n);
        }

        return accountNumber.toString();
    }
}
