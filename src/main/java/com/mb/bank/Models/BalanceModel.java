package com.mb.bank.Models;

import jakarta.persistence.Entity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceModel {
    

    private Long customerId;
    private Long accountNumber;
    private Long transactionAmount;
    private String transactionType;
    private Long currentBalance;


}
