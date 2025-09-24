package com.room_rent.Room_Rent_Application.dto.room;

import java.math.BigDecimal;
import java.util.List;

public record RoomResponse(Long id,
                           BigDecimal price,
                           String waterFacility,
                           String contactNo,
                           String location,
                           String booking,
                           String shareType,
                           List<String> mediaUrls,
                           String createdBy) {
}
