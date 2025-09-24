package com.room_rent.Room_Rent_Application.dto;

import java.io.Serializable;

    public record VerifyOtpRequest(String otp,String email) implements Serializable {
    }
