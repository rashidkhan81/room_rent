package com.room_rent.Room_Rent_Application.exception;

import com.room_rent.Room_Rent_Application.Global.GlobalApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {



        // Handle IllegalArgumentException
        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<GlobalApiResponse<String>> handleIllegalArgument(IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(GlobalApiResponse.error(ex.getMessage(), 400));
        }

        // Handle IllegalStateException
        @ExceptionHandler(IllegalStateException.class)
        public ResponseEntity<GlobalApiResponse<String>> handleIllegalState(IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(GlobalApiResponse.error(ex.getMessage(), 403));
        }

        // Handle UsernameNotFoundException
        @ExceptionHandler(UsernameNotFoundException.class)
        public ResponseEntity<GlobalApiResponse<String>> handleUserNotFound(UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(GlobalApiResponse.error(ex.getMessage(), 404));
        }

        // Fallback - handle all other exceptions
        @ExceptionHandler(Exception.class)
        public ResponseEntity<GlobalApiResponse<String>> handleGeneralException(Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(GlobalApiResponse.error("Internal Server Error: " + ex.getMessage(), 500));
        }




}
