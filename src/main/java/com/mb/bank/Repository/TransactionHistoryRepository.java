package com.mb.bank.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mb.bank.Entity.TransactionHistory;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
    
    @Query(value = "SELECT * FROM transaction_history WHERE customer_id = :customerId", nativeQuery = true)
    List<TransactionHistory> getAllByCustomerId(@Param("customerId") Long customerId);
}
