package com.mb.bank.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferModel {
    
    private Long customerId;
    private Long accountNumber;
    private Long recpientId;
    private Long recipientAccountNumber;
    private String transactionType;
    private Long transactionAmount;
    private Long currentBalance;
}
