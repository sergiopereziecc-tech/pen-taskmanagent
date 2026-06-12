package com.pen.taskmanagement.exceptions;

import java.time.LocalDateTime;
import java.util.Map;

import java.util.stream.Collectors;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.pen.taskmanagement.dtos.ApiError;
import com.pen.taskmanagement.dtos.ApiValidationError;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request){

        ApiError apiError = new ApiError(ex.getMessage(), request.getDescription(false), 
            LocalDateTime.now());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneral(Exception ex, WebRequest request){

        ApiError apiError = new ApiError("Internal Error. Contact ADMIN", request.getDescription(false),
            LocalDateTime.now());
        
        return ResponseEntity.internalServerError().body(apiError);
        
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiValidationError> handleValidation(MethodArgumentNotValidException ex, WebRequest request){
        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (e,r) -> e));
        
        return ResponseEntity.badRequest().body(new ApiValidationError("Validation failed",
         request.getDescription(false), LocalDateTime.now(), fieldErrors));

    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiError> handlePermission(ForbiddenException ex, WebRequest request){
        ApiError apiError = new ApiError(ex.getMessage(), request.getDescription(false), LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleData(DataIntegrityViolationException ex, WebRequest request){
        ApiError apiError = new ApiError("User already exists", request.getDescription(false), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }
}
