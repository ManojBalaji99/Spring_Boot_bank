package com.mb.bank.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mb.bank.Entity.Customer;
import com.mb.bank.Entity.TransactionHistory;
import com.mb.bank.Models.BalanceEnquiryModel;
import com.mb.bank.Models.BalanceModel;
import com.mb.bank.Models.TransferModel;
import com.mb.bank.Services.CustomerService;

@RestController
@RequestMapping("/api")
public class BankController {
    
    @Autowired
    private CustomerService customerService;

    @GetMapping("customer")
    public List<Customer> getCustomers() {
        return customerService.getCustomers();
    }
    
    @PostMapping("customer")
    public Customer saveCustomer(@RequestBody Customer customer) {
        return customerService.saveCustomer(customer);
    }


    // withdrawl Enquiry

    @GetMapping("customer/balance/{id}")
    public BalanceEnquiryModel balanceEnquiry(@PathVariable Long id) {
        return customerService.balanceEnquiry(id);
    }

    @PutMapping("customer/{id}/deposit/{amount}")
    public BalanceModel depositAmount(@PathVariable Long id, @PathVariable Long amount) {
        return customerService.depositAmount(id, amount);
    }

    @PutMapping("customer/{id}/withdrawl/{amount}")
    public BalanceModel withdrawlAmount(@PathVariable Long id, @PathVariable Long amount) {
        return customerService.withdrawlAmount(id, amount);
    }

    @PutMapping("customer/{senderId}/recpient/{recpientId}/amount/{amount}")
    public TransferModel transferAmounts(@PathVariable Long senderId, @PathVariable Long recipientId,
            @PathVariable Long amount) {

        return customerService.transferAmounts(senderId, recipientId, amount);
    }
    

    @GetMapping("customer/history/{id}")
    public List<TransactionHistory> getHistorybyCustomer(@PathVariable Long id) {
        return customerService.getHistoryByCustomer(id);
    }

}
