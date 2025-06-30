package com.simplified.picpay.controller;

import org.springframework.http.ResponseEntity;
import com.simplified.picpay.model.domain.transaction.Transaction;
import com.simplified.picpay.model.dto.transaction.TransactionDTO;
import com.simplified.picpay.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService service;

    @PostMapping("/new-transaction")
    public ResponseEntity<Transaction> transaction(@RequestBody TransactionDTO transactionDTO) throws AccessDeniedException {
        Transaction newTransaction = service.createTransaction(transactionDTO);
        return ResponseEntity.status(202).body(newTransaction);
    }
}
