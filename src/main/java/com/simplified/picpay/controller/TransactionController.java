package com.simplified.picpay.controller;

import com.simplified.picpay.model.domain.transaction.Transaction;
import com.simplified.picpay.model.dto.transaction.TransactionDTO;
import com.simplified.picpay.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService service;

    @PostMapping("/new-transaction")
    public ResponseEntity<?> transaction(@RequestBody TransactionDTO transactionDTO) {
        try {
            Transaction newTransaction = service.createTransaction(transactionDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "✅ Transação realizada com sucesso!");
            response.put("transaction", newTransaction);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "❌ Erro ao processar transação: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
