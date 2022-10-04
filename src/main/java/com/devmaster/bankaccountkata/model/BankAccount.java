package com.devmaster.bankaccountkata.model;

import com.devmaster.bankaccountkata.utils.AccountUtils;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankAccount {

    private String accountNum = AccountUtils.generateBankAccount();
    private LocalDate creationDate = LocalDate.now();
    private Client client;
    @Getter(onMethod_ = {@Synchronized})
    @Setter(onMethod_ = {@Synchronized})
    private BigDecimal balance = new BigDecimal(0);
    @Getter(onMethod_ = {@Synchronized})
    @Setter(onMethod_ = {@Synchronized})
    private List<Operation> operations = new ArrayList<>();



}
