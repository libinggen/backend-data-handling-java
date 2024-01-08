package com.libinggen.javadocker.javaapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        // Construct an error response object
        ErrorResponse errorResponse = new ErrorResponse("INTERNAL_SERVER_ERROR", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse("INVALID_ARGUMENT", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("RESOURCE_NOT_FOUND", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
        ErrorResponse errorResponse = new ErrorResponse("BAD_REQUEST", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SignatureVerificationException.class)
    public ResponseEntity<Object> handleSignatureVerificationException(
            SignatureVerificationException ex) {
        ErrorResponse errorResponse = new ErrorResponse("AUTHENTICATION_FAILED", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<Object> handleJWTVerificationException(JWTVerificationException ex) {
        // Log the exception details for debugging purposes
        // logger.error("JWT Verification failed: {}", ex.getMessage());

        // Construct an error response
        ErrorResponse errorResponse = new ErrorResponse("AUTHENTICATION_FAILED", ex.getMessage());

        // Return the response entity with a suitable HTTP status
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }


    // Inner class for error response
    private static class ErrorResponse {
        private String errorCode;
        private String errorMessage;

        public ErrorResponse(String errorCode, String errorMessage) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
        }

        // Getters
    }
}

