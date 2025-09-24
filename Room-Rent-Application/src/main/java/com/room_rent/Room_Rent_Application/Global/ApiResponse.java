package com.room_rent.Room_Rent_Application.Global;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private String status;   // SUCCESS, ERROR, FAILURE
    private int code;        // HTTP status code
    private String message;  // Success/error message
    private T data;          // Generic data payload
    private LocalDateTime timestamp; // response time

    public static <T> ApiResponse<T> success(String message, T data, int code) {
        return ApiResponse.<T>builder()
                .status("SUCCESS")
                .code(code)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(String message, int code) {
        return ApiResponse.<T>builder()
                .status("ERROR")
                .code(code)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}