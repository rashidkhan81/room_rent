package com.room_rent.Room_Rent_Application.service.booking;

import com.room_rent.Room_Rent_Application.dto.booking.BookingRequestPojo;
import com.room_rent.Room_Rent_Application.dto.booking.BookingResponseProjection;
import com.room_rent.Room_Rent_Application.exception.NotFoundException;
import com.room_rent.Room_Rent_Application.message.CustomMessageSource;
import com.room_rent.Room_Rent_Application.model.booking.Booking;
import com.room_rent.Room_Rent_Application.paginationPageResponse.PagedResponse;
import com.room_rent.Room_Rent_Application.repository.booking.BookingRepository;
import com.room_rent.Room_Rent_Application.service.AuthService;
import com.room_rent.Room_Rent_Application.service.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.room_rent.Room_Rent_Application.message.SuccessResponseConstant.BOOKING;
import static com.room_rent.Room_Rent_Application.message.SuccessResponseConstant.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final CustomMessageSource customMessageSource;
    private final AuthService authService;
    private final RoomService roomService;

    @Override
    public Booking finById(Long id) {
        return bookingRepository.findById(id).orElseThrow(()->
                new NotFoundException(customMessageSource.get(NOT_FOUND,customMessageSource.get(BOOKING))));
    }

    @Override
    public BookingRequestPojo save(BookingRequestPojo bookingRequestPojo) {
        Booking booking = new Booking();
        booking.setRoom(roomService.findById(bookingRequestPojo.getRoomId()));
        booking.setUser(authService.findById(bookingRequestPojo.getUserId()));
        booking.setStatus(bookingRequestPojo.getStatus());
        bookingRepository.save(booking);
        return bookingRequestPojo;
    }

    @Override
    public PagedResponse<BookingResponseProjection> getAllBookings(int page, int size) {

        Pageable pageable = PageRequest.of(page , size);
        Page<BookingResponseProjection> responseProjections = bookingRepository.getAllBooking(pageable);

        PagedResponse.PageInfo pageInfo = new PagedResponse.PageInfo(
                responseProjections.getNumber(),           // current page
                responseProjections.getSize(),             // page size
                responseProjections.getTotalElements(),    // total elements
                responseProjections.getTotalPages(),       // total pages
                responseProjections.isLast()               // last page flag
        );

        return new PagedResponse<>(
                responseProjections.getContent(),
                pageInfo
        );


    }
}
