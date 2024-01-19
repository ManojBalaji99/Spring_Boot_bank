package com.mb.bank.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceEnquiryModel {

    private Long customerId;

    private String customerName;

    private Long accountNumber;

    private Long currentBalance;
    
} 
