package com.devmaster.bankaccountkata.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankAccount {

    private String accountNum = UUID.randomUUID().toString();
    private LocalDate creationDate = LocalDate.now();
    private Client client;
    @Getter(onMethod_ = {@Synchronized})
    @Setter(onMethod_ = {@Synchronized})
    private BigDecimal balance = new BigDecimal(0);
    @Getter(onMethod_ = {@Synchronized})
    @Setter(onMethod_ = {@Synchronized})
    private List<Operation> operationList = new ArrayList<>();

}
