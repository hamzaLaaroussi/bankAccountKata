package com.devmaster.bankaccountkata.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Operation {

    private BigDecimal amount;
    private OperationType type;
    private LocalDate operationDate;
    private BigDecimal balance;
}
