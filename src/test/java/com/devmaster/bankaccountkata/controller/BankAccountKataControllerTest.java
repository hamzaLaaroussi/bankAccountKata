package com.devmaster.bankaccountkata.controller;

import com.devmaster.bankaccountkata.model.BankAccount;
import com.devmaster.bankaccountkata.model.Client;
import com.devmaster.bankaccountkata.model.Operation;
import com.devmaster.bankaccountkata.model.OperationType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class BankAccountKataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    Map<String, BankAccount> data;

    @Test
    public void should_createNewAccount_When_createMethodIsCalled() throws Exception {
        Client client = Client.builder()
                .id(123456789)
                .firstName("Jon")
                .lastName("LEE")
                .build();
        BankAccount bankAccount = new BankAccount(); // Instantiate it for AccountNum and creationDate Initiation
        bankAccount.setClient(client);
        this.mockMvc
                .perform(post("/api/bankaccountkata/create").content(asJsonString(bankAccount))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNum").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.client.id").value(123456789))
                .andExpect(MockMvcResultMatchers.jsonPath("$.client.firstName").value("Jon"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.client.lastName").value("LEE"));
    }

    @Test
    public void should_ReturnOperation_When_callingDepositMethod() throws Exception {
        Client client = Client.builder()
                .id(123456789)
                .firstName("Jon")
                .lastName("LEE")
                .build();
        BankAccount bankAccount = new BankAccount();
        bankAccount.setClient(client);

        Mockito.when(data.values()).thenReturn(Collections.singleton(bankAccount));

        data.put(bankAccount.getAccountNum(), bankAccount);

        this.mockMvc
                .perform(get("/api/bankaccountkata/deposit")
                        .param("clientId", "123456789")
                        .param("amount", "1000"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("DEPOSIT"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(1000));

    }

    @Test
    public void should_throwError_When_clientAccountNotFound() throws Exception {
        Client client = Client.builder()
                .id(123456789)
                .firstName("Jon")
                .lastName("LEE")
                .build();

        BankAccount bankAccount = new BankAccount();
        bankAccount.setClient(client);
        bankAccount.setBalance(new BigDecimal(100));

        Mockito.when(data.values()).thenReturn(Collections.singleton(bankAccount));

        data.put(bankAccount.getAccountNum(), bankAccount);

        this.mockMvc
                .perform(get("/api/bankaccountkata/deposit")
                        .param("clientId", "111111111")
                        .param("amount", "120"))
                .andDo(print())
                .andExpect(status().isPreconditionFailed());

    }

    @Test
    public void should_returnOperation_When_callingWithdrawMethod() throws Exception {
        Client client = Client.builder().id(123456789).firstName("Jon").lastName("LEE").build();
        BankAccount bankAccount = new BankAccount();
        bankAccount.setClient(client);
        bankAccount.setBalance(new BigDecimal(100));

        Mockito.when(data.values()).thenReturn(Collections.singleton(bankAccount));

        data.put(bankAccount.getAccountNum(), bankAccount);

        this.mockMvc
                .perform(get("/api/bankaccountkata/withdraw")
                        .param("clientId", "123456789")
                        .param("amount", "50"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(50))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("WITHDRAWAL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(50));

    }


    @Test
    public void should_throwError_When_amountIsBiggerThanBalance() throws Exception {
        Client client = Client.builder().id(123456789).firstName("Jon").lastName("LEE").build();
        BankAccount bankAccount = new BankAccount();
        bankAccount.setClient(client);
        bankAccount.setBalance(new BigDecimal(100));

        Mockito.when(data.values()).thenReturn(Collections.singleton(bankAccount));

        data.put(bankAccount.getAccountNum(), bankAccount);

        this.mockMvc
                .perform(get("/api/bankaccountkata/withdraw")
                        .param("clientId", "123456789")
                        .param("amount", "120"))
                .andDo(print())
                .andExpect(status().isPreconditionFailed());

    }

    @Test
    public void should_returnOperationList_When_callingWithdrawMethod() throws Exception {
        Client client = Client.builder().id(123456789).firstName("Jon").lastName("LEE").build();
        BankAccount bankAccount = new BankAccount();
        bankAccount.setClient(client);
        bankAccount.setBalance(new BigDecimal(100));

        Operation operationDeposit = Operation.builder().amount(new BigDecimal(110)).type(OperationType.DEPOSIT).balance(new BigDecimal(210)).build();
        Operation operationWithdraw = Operation.builder().amount(new BigDecimal(10)).type(OperationType.WITHDRAWAL).balance(new BigDecimal(200)).build();

        bankAccount.setOperationList(Arrays.asList(operationDeposit, operationWithdraw));
        Mockito.when(data.values()).thenReturn(Collections.singleton(bankAccount));

        data.put(bankAccount.getAccountNum(), bankAccount);

        this.mockMvc
                .perform(get("/api/bankaccountkata/operations/{clientId}", 123456789))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type").value("DEPOSIT"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].amount").value(110))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].type").value("WITHDRAWAL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].amount").value(10));

    }

    @Test
    public void should_printStatement_When_callingStatementMethod() throws Exception {
        Client client = Client.builder().id(123456789).firstName("Jon").lastName("LEE").build();
        BankAccount bankAccount = new BankAccount();
        bankAccount.setClient(client);
        bankAccount.setBalance(new BigDecimal(100));

        Operation operationDeposit = Operation.builder()
                .amount(new BigDecimal(110))
                .type(OperationType.DEPOSIT)
                .balance(new BigDecimal(210))
                .operationDate(LocalDate.of(2022, 9, 22))
                .build();
        Operation operationWithdraw = Operation.builder()
                .amount(new BigDecimal(10))
                .type(OperationType.WITHDRAWAL)
                .balance(new BigDecimal(200))
                .operationDate(LocalDate.of(2022, 9, 23))
                .build();

        bankAccount.setOperationList(Arrays.asList(operationDeposit, operationWithdraw));
        Mockito.when(data.values()).thenReturn(Collections.singleton(bankAccount));

        data.put(bankAccount.getAccountNum(), bankAccount);

        this.mockMvc
                .perform(get("/api/bankaccountkata/statement/{clientId}", 123456789))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("date|operation|amount|balance|")))
                .andExpect(content().string(Matchers.containsString("22/09/2022|DEPOSIT|110|210|")))
                .andExpect(content().string(Matchers.containsString("23/09/2022|WITHDRAWAL|10|200|")));

    }

    public static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = JsonMapper.builder()
                    .addModule(new JavaTimeModule()) // Jackson module needed to support Java 8 Date/time
                    .build();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
