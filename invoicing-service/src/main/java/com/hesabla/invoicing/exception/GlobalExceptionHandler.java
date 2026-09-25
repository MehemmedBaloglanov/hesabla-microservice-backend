package com.hesabla.invoicing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @RestControllerAdvice = bütün @RestController-lərə tətbiq olunan
 * MƏRKƏZİ exception handler. auth-service-də hər controller öz içində
 * @ExceptionHandler yazırdı (o da düzgündür, kiçik servis üçün), amma
 * burada bir neçə controller (Customer/Product/Invoice) olacağı üçün
 * mərkəzləşdirmək təkrarı aradan qaldırır.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
