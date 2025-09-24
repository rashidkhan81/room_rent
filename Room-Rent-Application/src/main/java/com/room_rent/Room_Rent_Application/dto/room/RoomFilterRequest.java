package com.room_rent.Room_Rent_Application.dto.room;

import java.math.BigDecimal;

public record RoomFilterRequest(String location,
                                BigDecimal minPrice,
                                BigDecimal maxPrice) {
}
