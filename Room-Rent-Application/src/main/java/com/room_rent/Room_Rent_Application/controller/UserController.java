package com.room_rent.Room_Rent_Application.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @GetMapping("/profile")
    public String profile() {
        return "This is the User Profile page 👤 (Users & Admins)";
    }

    @GetMapping("/orders")
    public List<String> orders() {
        return List.of("Order A", "Order B", "Order C");
    }
}
