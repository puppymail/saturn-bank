package com.saturn_bank.operator.repository;

import com.saturn_bank.operator.dao.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySrc(String src);
    List<Transaction> findByDst(String dst);
}
