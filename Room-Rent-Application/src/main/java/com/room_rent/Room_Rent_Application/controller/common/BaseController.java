package com.room_rent.Room_Rent_Application.controller.common;

import com.room_rent.Room_Rent_Application.Global.GlobalApiResponse;
import com.room_rent.Room_Rent_Application.message.CustomMessageSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class BaseController {

    @Autowired
    protected CustomMessageSource customMessageSource;

    // ✅ Success Responses
    protected <T> GlobalApiResponse<T> successResponse(String message, T data) {
        return GlobalApiResponse.success(message, data, 200);
    }

    protected <T> GlobalApiResponse<T> successResponse(String message) {
        return GlobalApiResponse.success(message, 200);
    }

    // ✅ Error Responses
    protected GlobalApiResponse<Object> errorResponse(String message, List<String> errors, int code) {
        return GlobalApiResponse.error(message, errors, code);
    }

    protected GlobalApiResponse<Object> errorResponse(String message, int code) {
        return GlobalApiResponse.error(message, code);
    }
}
