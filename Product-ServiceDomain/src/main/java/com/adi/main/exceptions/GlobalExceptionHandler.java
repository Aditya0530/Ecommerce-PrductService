package com.adi.main.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.adi.main.dto.ErrorDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ProductException .class)
    public ResponseEntity<ErrorDto> handleInvalidProductException(ProductException ex) {
        LOG.error("Handling InvalidProductException: {}", ex.getMessage());
        ErrorDto errorResponse = new ErrorDto(ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductNotSavedException.class)
    public ResponseEntity<ErrorDto> handleProductNotSavedException(ProductNotSavedException ex) {
        LOG.error("Handling ProductNotSavedException: {}", ex.getMessage());
        ErrorDto errorResponse = new ErrorDto("An unexpected error occurred", LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGenericException(Exception ex) {
        LOG.error("Handling Generic Exception: {}", ex.getMessage(), ex);
        ErrorDto errDto = new ErrorDto("An error occurred: " + ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        LOG.error("Validation error: {}", ex.getMessage());
        ErrorDto errorResponse = new ErrorDto("Validation failed", LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    } 
}
