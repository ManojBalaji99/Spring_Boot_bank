package com.mb.bank.Services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.mb.bank.Entity.Customer;
import com.mb.bank.Entity.TransactionHistory;
import com.mb.bank.Exception.CustomerNotFoundException;
import com.mb.bank.Exception.InvalidAmountException;
import com.mb.bank.Exception.NoSuffcientBalanceAmountException;
import com.mb.bank.Exception.TransactionHistoryNotFoundException;
import com.mb.bank.Models.BalanceEnquiryModel;
import com.mb.bank.Models.BalanceModel;
import com.mb.bank.Models.TransferModel;
import com.mb.bank.Repository.CustomerRepository;
import com.mb.bank.Repository.TransactionHistoryRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    public Customer saveCustomer(Customer customer) {

        return customerRepository.save(customer);
    }

    // balance enquiry

    public BalanceEnquiryModel balanceEnquiry(Long id) {
        Customer customer = customerRepository.findById(id).get();
        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found with ID: " + id);
        }
        BalanceEnquiryModel model = new BalanceEnquiryModel();
        model.setCustomerId(customer.getCustomerId());
        model.setCustomerName(customer.getCustomerName());
        model.setAccountNumber(customer.getAccountNumber());
        model.setCurrentBalance(customer.getAccountBalance());
        return model;
    }

    // deposit amount
    @Transactional
    @Modifying
    public BalanceModel depositAmount(Long id, Long amount) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));

        if (amount <= 0) {
            throw new InvalidAmountException("Invalid deposit amount: " + amount);
        }

        Long balance = customer.getAccountBalance() + amount;

        // transaction history
        TransactionHistory transaction = new TransactionHistory();
        transaction.setCustomer(customer);
        transaction.setTransactionDateTime(LocalDateTime.now());
        transaction.setTransactionType("Deposited");
        transaction.setAmount(amount);
        customer.setAccountBalance(balance);

        List<TransactionHistory> customerHistory = customer.getTransactionHistory();
        customerHistory.add(transaction);
        customer.setTransactionHistory(customerHistory);
        customerRepository.save(customer);

        // balance model

        BalanceModel balanceModel = new BalanceModel();
        balanceModel.setCustomerId(customer.getCustomerId());
        balanceModel.setAccountNumber(customer.getAccountNumber());
        balanceModel.setTransactionAmount(amount);
        balanceModel.setTransactionType("Deposit");
        balanceModel.setCurrentBalance(balance);
        return balanceModel;

    }

    // withdrawl amount
    @Transactional
    @Modifying
    public BalanceModel withdrawlAmount(Long id, Long amount) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));

        if (amount <= 0 || customer.getAccountBalance() < amount) {
            throw new NoSuffcientBalanceAmountException("Insufficient balance for withdrawal");
        }

        Long balance = customer.getAccountBalance() - amount;
        TransactionHistory transaction = new TransactionHistory();
        transaction.setCustomer(customer);
        transaction.setTransactionDateTime(LocalDateTime.now());
        transaction.setTransactionType("Withdrawl");
        transaction.setAmount(amount);
        customer.setAccountBalance(balance);
        List<TransactionHistory> customerHistory = customer.getTransactionHistory();
        customerHistory.add(transaction);
        customer.setTransactionHistory(customerHistory);
        customerRepository.save(customer);
        // balanceModel
        BalanceModel balanceModel = new BalanceModel();
        balanceModel.setCustomerId(customer.getCustomerId());
        balanceModel.setAccountNumber(customer.getAccountNumber());
        balanceModel.setTransactionAmount(amount);
        balanceModel.setTransactionType("WithDrawl");
        balanceModel.setCurrentBalance(balance);
        return balanceModel;
    }

    // Transfering amount
    @Transactional
    @Modifying
    public TransferModel transferAmounts(Long senderId, Long recipientId, Long amount) {
        Customer sender = customerRepository.findById(senderId)
                .orElseThrow(() -> new CustomerNotFoundException("Sender not found with ID: " + senderId));

        Customer recipient = customerRepository.findById(recipientId)
                .orElseThrow(() -> new CustomerNotFoundException("Recipient not found with ID: " + recipientId));

        if (amount <= 0 || sender.getAccountBalance() < amount) {
            throw new IllegalArgumentException("Invalid transfer amount or insufficient balance");
        }

        sender.setAccountBalance(sender.getAccountBalance() - amount);
        recipient.setAccountBalance(recipient.getAccountBalance() + amount);

        TransactionHistory senderTransaction = new TransactionHistory();
        senderTransaction.setCustomer(sender);
        senderTransaction.setRecipient_sender(recipient.getCustomerName());
        senderTransaction.setTransactionDateTime(LocalDateTime.now());
        senderTransaction.setTransactionType("TRANSFER-sent");
        senderTransaction.setAmount(amount);

        TransactionHistory recipientTransaction = new TransactionHistory();
        recipientTransaction.setCustomer(recipient);
        recipientTransaction.setRecipient_sender(sender.getCustomerName());
        recipientTransaction.setTransactionDateTime(LocalDateTime.now());
        recipientTransaction.setTransactionType("TRANSFER-received");
        recipientTransaction.setAmount(amount);

        List<TransactionHistory> senderHistory = sender.getTransactionHistory();
        senderHistory.add(senderTransaction);
        List<TransactionHistory> recipientHistory = recipient.getTransactionHistory();
        recipientHistory.add(recipientTransaction);

        sender.setTransactionHistory(senderHistory);
        recipient.setTransactionHistory(recipientHistory);

        customerRepository.save(sender);
        customerRepository.save(recipient);

        // List<Customer> customers = new ArrayList<>();
        // customers.add(sender);
        // customers.add(recipient);
        // return customers;

        // transferModel

        TransferModel transferModel = new TransferModel();
        transferModel.setCustomerId(sender.getCustomerId());
        transferModel.setAccountNumber(sender.getAccountNumber());
        transferModel.setRecpientId(recipient.getCustomerId());
        transferModel.setRecipientAccountNumber(recipient.getAccountNumber());
        transferModel.setTransactionType("Bank Transfer");
        transferModel.setTransactionAmount(amount);
        transferModel.setCurrentBalance(sender.getAccountBalance());
        return transferModel;

    }

    // get transaction history

    public List<TransactionHistory> getHistoryByCustomer(Long id) {
        List<TransactionHistory> transactionHistory = transactionHistoryRepository.getAllByCustomerId(id);
        if (transactionHistory.isEmpty()) {
            throw new TransactionHistoryNotFoundException("No transaction history found for customer with ID: " + id);
        }
        return transactionHistory;
    }

}
