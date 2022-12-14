package com.devmaster.bankaccountkata.controller;

import com.devmaster.bankaccountkata.model.BankAccount;
import com.devmaster.bankaccountkata.model.Operation;
import com.devmaster.bankaccountkata.service.AccountManagement;
import com.devmaster.bankaccountkata.service.AccountOperations;
import com.devmaster.bankaccountkata.service.StatementPrinter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/bankaccountkata/")
@CrossOrigin(origins = {"http://localhost:4200/"})
public class BankAccountKataController {

    private final AccountManagement accountManagement;
    private final AccountOperations accountOperations;
    private final StatementPrinter statementPrinter;

    @Autowired
    public BankAccountKataController(AccountManagement accountManagement, AccountOperations accountOperations, StatementPrinter statementPrinter) {
        this.accountManagement = accountManagement;
        this.accountOperations = accountOperations;
        this.statementPrinter = statementPrinter;
    }

    @PostMapping("/create")
    public ResponseEntity<BankAccount> saveBankAccount(@RequestBody BankAccount bankAccount) {
        log.info("Start saving a new bank account");
        BankAccount newAccount = accountManagement.createBankAccount(bankAccount);
        return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<BankAccount> findBankAccount(@PathVariable("clientId") long clientId) {
        log.info("Start getting account by Id");
        BankAccount newAccount = accountManagement.findAccountByClientId(clientId);
        return new ResponseEntity<>(newAccount, HttpStatus.OK);
    }

    @PatchMapping("/deposit")
    public ResponseEntity<Operation> deposit(@RequestParam long clientId, @RequestParam BigDecimal amount) {
        log.info("Start deposit operation");
        BankAccount bankAccount = accountManagement.findAccountByClientId(clientId);
        Operation depositOperation = accountOperations.deposit(bankAccount, amount);
        return new ResponseEntity<>(depositOperation, HttpStatus.OK);
    }

    @PatchMapping("/withdraw")
    public ResponseEntity<Operation> withdraw(@RequestParam long clientId, @RequestParam BigDecimal amount) {
        log.info("Start withdrawal operation");
        BankAccount bankAccount = accountManagement.findAccountByClientId(clientId);
        Operation withdrawOperation = accountOperations.withdraw(bankAccount, amount);
        return new ResponseEntity<>(withdrawOperation, HttpStatus.OK);
    }

    @GetMapping("/operations/{clientId}")
    public ResponseEntity<List<Operation>> getOperationsHistory(@PathVariable("clientId") long clientId) {
        log.info("Start getting operations history");
        BankAccount bankAccount = accountManagement.findAccountByClientId(clientId);
        List<Operation> operations = accountOperations.checkHistory(bankAccount);
        return new ResponseEntity<>(operations, HttpStatus.OK);
    }

    @GetMapping("/statement/{clientId}")
    public ResponseEntity<String> getAccountStatement(@PathVariable("clientId") long clientId) {
        log.info("Start getting account statement");
        BankAccount bankAccount = accountManagement.findAccountByClientId(clientId);
        String statement = statementPrinter.printAccountStatement(bankAccount);
        return new ResponseEntity<>(statement, HttpStatus.OK);
    }


}
