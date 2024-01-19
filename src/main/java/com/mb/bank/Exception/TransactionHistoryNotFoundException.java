package com.mb.bank.Exception;

public class TransactionHistoryNotFoundException extends RuntimeException {
    
    public TransactionHistoryNotFoundException(String message) {
        super(message);
    }
}
