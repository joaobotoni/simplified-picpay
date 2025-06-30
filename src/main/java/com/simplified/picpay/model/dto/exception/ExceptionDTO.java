package com.simplified.picpay.model.dto.exception;

import org.springframework.http.HttpStatus;

public record ExceptionDTO(String message, HttpStatus status) {
}
