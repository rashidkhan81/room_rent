package com.room_rent.Room_Rent_Application.dto.booking;

import com.room_rent.Room_Rent_Application.enums.room.booking.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseProjection {
    private Long id;
    private Long roomId;
    private Long userId;

    private BookingStatus status;
}
