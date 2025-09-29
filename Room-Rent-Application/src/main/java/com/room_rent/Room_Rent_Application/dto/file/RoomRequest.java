package com.room_rent.Room_Rent_Application.dto.file;

import java.math.BigDecimal;

public record RoomRequest (BigDecimal price,
                           String waterFacility,
                           String contactNo,
                           String location,
                           String booking,
                           String shareType
) {}
