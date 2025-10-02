package com.room_rent.Room_Rent_Application.repository.booking;

import com.room_rent.Room_Rent_Application.dto.booking.BookingResponseProjection;
import com.room_rent.Room_Rent_Application.model.booking.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT NEW com.room_rent.Room_Rent_Application.dto.booking.BookingResponseProjection( " +
            "b.id,  b.room.id, b.user.id, b.status) " +
            "FROM Booking b ")
    Page<BookingResponseProjection> getAllBooking(
            Pageable pageable);


}
