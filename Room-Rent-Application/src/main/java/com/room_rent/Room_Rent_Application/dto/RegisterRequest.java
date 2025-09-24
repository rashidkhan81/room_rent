package com.room_rent.Room_Rent_Application.dto;

import java.io.Serializable;

public record RegisterRequest(String name, String email, String password,
                              String role ) implements Serializable {
}
