package com.simplified.picpay.service;

import com.simplified.picpay.model.domain.transaction.Transaction;
import com.simplified.picpay.model.domain.user.User;
import com.simplified.picpay.model.dto.transaction.TransactionDTO;
import com.simplified.picpay.model.repository.TransactionRepository;
import com.simplified.picpay.rest.AuthorizationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@Service
public class TransactionService {

    @Autowired
    private UserService service;

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private WebClient webClient;

    @Value("${util.auth.transaction.api.url}")
    private String url;

    @Autowired
    private NotificationService notificationService;

    public Transaction createTransaction(TransactionDTO dto) throws AccessDeniedException {
        User sender = service.getUserById(dto.senderId());
        User receiver = service.getUserById(dto.receiverId());

        service.validateTransaction(sender, dto.value());

        if (!authorizeTransaction(sender, dto.value())) {
            throw new AccessDeniedException("Transação não autorizada");
        }

        Transaction transaction = buildTransaction(sender, receiver, dto.value());

        updateUserBalances(sender, receiver, dto.value());

        repository.save(transaction);
        service.save(sender);
        service.save(receiver);

        notifyUsers(sender, receiver);

        return transaction;
    }


    public boolean authorizeTransaction(User sender, BigDecimal value) {

        ResponseEntity<AuthorizationResponse> responseEntity = webClient
                .get()
                .uri(url)
                .exchangeToMono(response -> response.toEntity(AuthorizationResponse.class))
                .block();

        AuthorizationResponse authorizationResponse = responseEntity != null ? responseEntity.getBody() : null;

        return authorizationResponse != null
                && "success".equalsIgnoreCase(authorizationResponse.status())
                && authorizationResponse.data() != null
                && authorizationResponse.data().authorization();
    }

    private Transaction buildTransaction(User sender, User receiver, BigDecimal value) {
        Transaction tx = new Transaction();
        tx.setSender(sender);
        tx.setReceiver(receiver);
        tx.setAmount(value);
        tx.setTime(LocalDateTime.now());
        return tx;
    }

    private void updateUserBalances(User sender, User receiver, BigDecimal value) {
        sender.setBalance(sender.getBalance().subtract(value));
        receiver.setBalance(receiver.getBalance().add(value));
    }

    private void notifyUsers(User sender, User receiver) {
        notificationService.sendNotification(sender, "Transaction completed successfully");
        notificationService.sendNotification(receiver, "Transaction received successfully");
    }

}
