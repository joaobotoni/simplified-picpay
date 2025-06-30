package com.simplified.picpay.service;

import com.simplified.picpay.model.domain.user.User;
import com.simplified.picpay.model.dto.notification.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class NotificationService {
    @Autowired
    private WebClient webClient;

    @Value("${util.notify.api.url}")
    private String url;

    public void sendNotification(User user, String message) {
        NotificationDTO notification = new NotificationDTO(user.getEmail(), message);

        try {
//            ResponseEntity<String> response = webClient.post()
//                    .uri(url)
//                    .bodyValue(notification)
//                    .exchangeToMono(resp -> resp.toEntity(String.class))
//                    .block();
//
//            if (response == null || !response.getStatusCode().equals(HttpStatus.OK)) {
//                System.out.println("Erro ao enviar mensagem");
//                throw new RuntimeException("Serviço de notificação está fora do ar");
//            }
            System.out.print("Transaction completed successfully");
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
