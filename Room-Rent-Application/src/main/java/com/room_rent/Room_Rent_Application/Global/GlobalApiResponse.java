package com.room_rent.Room_Rent_Application.Global;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GlobalApiResponse<T> implements Serializable {

    private boolean status;          // true = success, false = error
    private int code;                // HTTP status code
    private String message;          // success or error message
    private T data;                  // actual payload
    private List<String> errors;     // validation / exception errors
    private LocalDateTime timestamp; // time of response

    // --- Factory methods for cleaner usage ---
    public static <T> GlobalApiResponse<T> success(String message, T data, int code) {
        return GlobalApiResponse.<T>builder()
                .status(true)
                .code(code)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> GlobalApiResponse<T> success(String message, int code) {
        return success(message, null, code);
    }

    public static <T> GlobalApiResponse<T> error(String message, List<String> errors, int code) {
        return GlobalApiResponse.<T>builder()
                .status(false)
                .code(code)
                .message(message)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> GlobalApiResponse<T> error(String message, int code) {
        return error(message, null, code);
    }
}
