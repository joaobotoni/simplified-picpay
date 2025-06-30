package com.simplified.picpay.model.dto.notification;

import org.apache.logging.log4j.message.Message;

public record NotificationDTO(String email, String message) {
}
