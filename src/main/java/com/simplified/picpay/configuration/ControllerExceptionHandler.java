package com.simplified.picpay.configuration;

import com.simplified.picpay.model.dto.exception.ExceptionDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> threatDuplicateEntry(DataIntegrityViolationException entry) {
        ExceptionDTO exceptionDTO = new ExceptionDTO("User and registered", HttpStatus.NOT_FOUND);
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> threatNotFound(DataIntegrityViolationException entry) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> threatGlobalRuntimeException(RuntimeException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.internalServerError().body(exceptionDTO);
    }
}
