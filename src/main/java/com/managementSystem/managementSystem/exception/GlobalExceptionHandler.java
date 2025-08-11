package com.managementSystem.managementSystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
//        Map<String, Object> body = new LinkedHashMap<>();
//        body.put("timestamp", ZonedDateTime.now());
//        body.put("status", HttpStatus.NOT_FOUND.value());
//        body.put("error", "Not Found");
//        body.put("message", ex.getMessage());
//
//        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
//    }

    @RestControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        }
    }


