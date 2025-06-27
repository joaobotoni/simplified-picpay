package com.simplified.picpay.model.repository;

import com.simplified.picpay.model.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
