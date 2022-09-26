package com.devmaster.bankaccountkata.service;


import com.devmaster.bankaccountkata.data.AccountListStoreConfiguration;
import com.devmaster.bankaccountkata.exception.BankOperationFailureException;
import com.devmaster.bankaccountkata.model.BankAccount;
import com.devmaster.bankaccountkata.model.Client;
import com.devmaster.bankaccountkata.service.impl.AccountManagementImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(AccountManagementImpl.class)
@Import(AccountListStoreConfiguration.class)
class AccountManagementTest {

    @InjectMocks
    AccountManagementImpl accountManagement;

    @Mock
    Map<String, BankAccount> data;


    @Test
    void should_accountBeCreated_when_createNewAccount() {
        Client client = Client.builder()
                .id(123456789)
                .firstName("Jon")
                .lastName("LEE")
                .tel("0681895421")
                .address("Paris")
                .build();
        BankAccount bankAccount = new BankAccount(); //Instantiate it for AccountNum and creationDate Initiation
        bankAccount.setClient(client);

        BankAccount createdBankAccount = accountManagement.createBankAccount(bankAccount);

        assertNotNull(createdBankAccount);
        assertNotNull(createdBankAccount.getAccountNum());
        assertNotNull(createdBankAccount.getCreationDate());
        assertNotNull(createdBankAccount.getClient());
        assertEquals(client.getId(), createdBankAccount.getClient().getId());
        assertEquals(client.getTel(), createdBankAccount.getClient().getTel());
        assertEquals(client.getAddress(), createdBankAccount.getClient().getAddress());
        assertEquals(client.getFirstName(), createdBankAccount.getClient().getFirstName());
        assertEquals(client.getLastName(), createdBankAccount.getClient().getLastName());
    }

    @Test
    void should_bankAccountBeFound_when_searchByClientId() {
        Client client1 = Client.builder()
                .id(11111111)
                .firstName("Melissa")
                .lastName("COOPER")
                .tel("0681895421")
                .address("Paris")
                .build();
        BankAccount bankAccount1 = new BankAccount(); //Instantiate it for AccountNum and creationDate Initiation
        bankAccount1.setClient(client1);

        Client client2 = Client.builder()
                .id(2222222)
                .firstName("Marc")
                .lastName("CRUZ")
                .tel("06587451236")
                .address("New York")
                .build();
        BankAccount bankAccount2 = new BankAccount(); //Instantiate it for AccountNum and creationDate Initiation
        bankAccount2.setClient(client2);


        Mockito.when(data.values()).thenReturn(Arrays.asList(bankAccount1, bankAccount2));

        BankAccount createdBankAccount = accountManagement.findAccountByClientId(client1.getId());

        assertNotNull(createdBankAccount);
        assertNotNull(createdBankAccount.getAccountNum());
        assertNotNull(createdBankAccount.getCreationDate());
        assertNotNull(createdBankAccount.getClient());
        assertEquals(client1.getId(), createdBankAccount.getClient().getId());
        assertEquals(client1.getTel(), createdBankAccount.getClient().getTel());
        assertEquals(client1.getAddress(), createdBankAccount.getClient().getAddress());
        assertEquals(client1.getFirstName(), createdBankAccount.getClient().getFirstName());
        assertEquals(client1.getLastName(), createdBankAccount.getClient().getLastName());
    }

    @Test
    void should_throwsError_when_searchByWrongClientId() {
        Client client = Client.builder()
                .id(11111111)
                .firstName("Melissa")
                .lastName("COOPER")
                .tel("0681895421")
                .address("Paris")
                .build();
        BankAccount bankAccount = new BankAccount(); //Instantiate it for AccountNum and creationDate Initiation
        bankAccount.setClient(client);


        Mockito.when(data.values()).thenReturn(Collections.singleton(bankAccount));

        long wrongClientId = 222222222;

        BankOperationFailureException bankOperationFailureException = assertThrows(BankOperationFailureException.class, () -> {
            accountManagement.findAccountByClientId(wrongClientId);
        });

        assertEquals("No bank account found with the client id: " + wrongClientId, bankOperationFailureException.getMessage());


    }
}
