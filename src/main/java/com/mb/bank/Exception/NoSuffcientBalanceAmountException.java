package com.mb.bank.Exception;

public class NoSuffcientBalanceAmountException extends RuntimeException {
    
    public NoSuffcientBalanceAmountException(String message) {
    super(message);
   }
}
