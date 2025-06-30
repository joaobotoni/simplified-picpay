package com.simplified.picpay.service;

import com.simplified.picpay.model.domain.transaction.Transaction;
import com.simplified.picpay.model.domain.user.User;
import com.simplified.picpay.model.dto.transaction.TransactionDTO;
import com.simplified.picpay.model.repository.TransactionRepository;
import com.simplified.picpay.rest.AuthorizationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {

    @Autowired
    private UserService service;

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AuthorizationResponse auth;

    @Value("${util.auth.transaction.api.url}")
    private String url;

    private void transaction(TransactionDTO transaction) {
        User sender = service.getUserById(transaction.senderId());
        User receiver = service.getUserById(transaction.receiverId());

        service.validateTransaction(sender, transaction.value());
        boolean isAuthorized = this.authorizeTransaction(sender, transaction.value());

        if (!isAuthorized) {
            throw new RuntimeException("Transação não autorizada");
        }

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTime(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        this.repository.save(newTransaction);
        this.service.save(sender);
        this.service.save(receiver);
    }

    public boolean authorizeTransaction(User sender, BigDecimal value) {
        ResponseEntity<AuthorizationResponse> response = restTemplate.getForEntity(url, AuthorizationResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            AuthorizationResponse authorizationResponse = response.getBody();
            if (authorizationResponse != null && "success".equals(authorizationResponse.status())) {
                return authorizationResponse.data().authorization();
            }
        }
        return false;
    }

}
