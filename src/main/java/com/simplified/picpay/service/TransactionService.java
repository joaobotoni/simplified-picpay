package com.simplified.picpay.service;

import com.simplified.picpay.model.domain.user.User;
import com.simplified.picpay.model.dto.transaction.TransactionDTO;
import com.simplified.picpay.model.repository.TransactionRepository;
import com.simplified.picpay.rest.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private UserService service;
    @Autowired
    private TransactionRepository repository;
    @Autowired
    private RestTemplate template;
    @Autowired
    private Authorization auth;
    private void transaction(TransactionDTO dto) {
        User sender = service.getUserById(dto.senderId());
        User receiver = service.getUserById(dto.receiverId());
        service.validateTransaction(sender, dto.value());
    }

    public boolean authorizeTransaction(User sender, BigDecimal value) {
         ResponseEntity<Map> authorizationResponse = template.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);
        return authorizationResponse.getStatusCode().equals(auth.status()) && auth.authorization().authorize();
    }
}
