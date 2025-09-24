package com.room_rent.Room_Rent_Application.dto;



public record AuthResponse(String token, String refreshToken) {
}

// if we do not create the record i would hava created this so better create the record-->

//public class AuthResponse {
//    private String accessToken;
//    private String refreshToken;
//
//    public AuthResponse(String accessToken, String refreshToken) {
//        this.accessToken = accessToken;
//        this.refreshToken = refreshToken;
//    }
//
//    // getters and setters