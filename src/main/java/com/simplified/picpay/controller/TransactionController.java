package com.simplified.picpay.controller;


import com.simplified.picpay.model.domain.transaction.Transaction;
import com.simplified.picpay.model.dto.transaction.TransactionDTO;
import com.simplified.picpay.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService service;

    @PostMapping("/new-transaction")
    private ResponseEntity<Transaction> transaction(@RequestBody TransactionDTO transaction) throws RuntimeException{
         Transaction newTransaction = this.service.createTransaction(transaction);
         return new ResponseEntity<>(newTransaction, HttpStatus.OK);
    }
}
