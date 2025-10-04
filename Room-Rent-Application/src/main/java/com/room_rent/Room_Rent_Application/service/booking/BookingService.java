package com.room_rent.Room_Rent_Application.service.booking;

import com.room_rent.Room_Rent_Application.dto.booking.BookingRequestPojo;
import com.room_rent.Room_Rent_Application.dto.booking.BookingResponseProjection;
import com.room_rent.Room_Rent_Application.enums.room.booking.BookingStatus;
import com.room_rent.Room_Rent_Application.model.booking.Booking;
import com.room_rent.Room_Rent_Application.paginationPageResponse.PagedResponse;
import jakarta.mail.MessagingException;

public interface BookingService {

    Booking finById(Long id);
    BookingRequestPojo save(BookingRequestPojo bookingRequestPojo) throws MessagingException;
    PagedResponse<BookingResponseProjection> getAllBookings(int page, int size);

    void deleteBooking(Long id);





    void updateBookingStatus(Long bookingId, BookingStatus status) throws MessagingException;
}
