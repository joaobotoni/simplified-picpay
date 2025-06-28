package com.simplified.picpay.rest;

import org.springframework.http.HttpStatus;

public record Authorization(HttpStatus status, IsAuthorization authorization) {
}
