package com.room_rent.Room_Rent_Application.controller;


import com.room_rent.Room_Rent_Application.dto.*;
import com.room_rent.Room_Rent_Application.security.JwtUtil;
import com.room_rent.Room_Rent_Application.service.AuthService;
import com.room_rent.Room_Rent_Application.service.TokenBlacklistService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final TokenBlacklistService tokenBlacklistService;
    private final JwtUtil jwtUtil;

//    public AuthController(AuthService authService) {
//        this.authService = authService;
//    }
//    public AuthController(AuthService authService) {
//        this.authService = authService;
//    }


//http://localhost:8080/api/auth/register
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        String message = authService.register(req);
        return ResponseEntity.ok(Map.of("message", message));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }



//http://localhost:8080/api/auth/forgot-password?email=rashidraeen82@gmail.com
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) throws MessagingException {
        authService.createPasswordResetToken(email);
        return ResponseEntity.ok("Check your email for reset link");
    }

    //Reset password 1

//http://localhost:8080/api/auth/reset-password?token=6776f032-1ed4-411e-8909-c3c6390633ca&newPassword=hamza@123
//    @PostMapping("/reset-password")
//    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
//        authService.resetPassword(token, newPassword);
//        return ResponseEntity.ok("Password updated");
//    }

//  Reset password 2
    //this is other api where i can send reset password things --> token and newPassword in requestBody

   //{
   //    "token":"9b1f61b7-0af5-4e7f-b272-2e205d8c6f03",
   //    "newPassword":"ayan@123"
   //}
@PostMapping("/reset-password")
public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
    authService.resetPassword(request.getToken(), request.getNewPassword());
    return ResponseEntity.ok("Password updated");
}


// this api is not being used so wait =====>
    @GetMapping("/reset-password-page")
    public String showResetPasswordPage(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "rasid"; // Thymeleaf template
    }



    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request.refreshToken()));
    }

//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(@RequestBody RefreshTokenRequest request) {
//        authService.logout(request.refreshToken());
//        return ResponseEntity.ok("Logged out successfully");
//    }


    //redis.....
//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(@RequestHeader("Authorization") String bearerToken,
//                                         @RequestBody RefreshTokenRequest request) {
//        String accessToken = bearerToken.replace("Bearer ", "");
//
//        // calculate remaining time of access token
//        long expirationMillis = jwtUtil.getExpirationMillis(accessToken);
//
//        // add to blacklist until it naturally expires
//        tokenBlacklistService.blacklistToken(accessToken, expirationMillis);
//
//        // remove refresh token from DB
//        authService.logout(request.refreshToken());
//
//        return ResponseEntity.ok("Logged out successfully");
//    }



}


/*
Our Application working flow
Flow of the Application
Registration (/api/auth/register)
AuthController → AuthService.register()
Saves User with role in DB
Login (/api/auth/login)
AuthController → AuthService.login()
Returns { token, refreshToken } in AuthResponse
Access Protected Endpoints
Request comes with Authorization: Bearer <token>
JwtAuthenticationFilter validates token
SecurityConfig checks role (ROLE_USER / ROLE_ADMIN)
Refresh Token (/api/auth/refresh)
AuthController → AuthService.refreshToken()
Validate refresh token → generate new access token (+ optionally new refresh token)
Forgot / Reset Password
/forgot-password → create PasswordResetToken → send email
/reset-password → validate token → update User.password  */