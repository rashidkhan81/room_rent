package com.room_rent.Room_Rent_Application.service.booking;

import com.room_rent.Room_Rent_Application.dto.booking.BookingRequestPojo;
import com.room_rent.Room_Rent_Application.dto.booking.BookingResponseProjection;
import com.room_rent.Room_Rent_Application.enums.room.booking.BookingStatus;
import com.room_rent.Room_Rent_Application.exception.NotFoundException;
import com.room_rent.Room_Rent_Application.exception.UnauthorizedException;
import com.room_rent.Room_Rent_Application.jwtTokenUtils.JwtTokenUtil;
import com.room_rent.Room_Rent_Application.message.CustomMessageSource;
import com.room_rent.Room_Rent_Application.model.User;
import com.room_rent.Room_Rent_Application.model.booking.Booking;
import com.room_rent.Room_Rent_Application.model.room.Room;
import com.room_rent.Room_Rent_Application.paginationPageResponse.PagedResponse;
import com.room_rent.Room_Rent_Application.repository.UserRepository;
import com.room_rent.Room_Rent_Application.repository.booking.BookingRepository;
import com.room_rent.Room_Rent_Application.service.AuthService;
import com.room_rent.Room_Rent_Application.service.EmailService;
import com.room_rent.Room_Rent_Application.service.room.RoomService;
import jakarta.mail.MessagingException;
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
    private final JwtTokenUtil jwtTokenUtil;
    private final EmailService emailService;
    private final UserRepository userRepository;

    @Override
    public Booking finById(Long id) {
        return bookingRepository.findById(id).orElseThrow(()->
                new NotFoundException(customMessageSource.get(NOT_FOUND,customMessageSource.get(BOOKING))));
    }

//    @Override
//    public BookingRequestPojo save(BookingRequestPojo bookingRequestPojo) {
//        Booking booking = new Booking();
//        booking.setRoom(roomService.findById(bookingRequestPojo.getRoomId()));
//        // Get logged-in user directly from JWT
//        booking.setUser(jwtTokenUtil.getLoggedInUser());
//        booking.setStatus(bookingRequestPojo.getStatus());
//        bookingRepository.save(booking);
//        return bookingRequestPojo;
//    }

    @Override
    public BookingRequestPojo save(BookingRequestPojo bookingRequestPojo) throws MessagingException {
        Booking booking = new Booking();

        // Find room and assign
        Room room = roomService.findById(bookingRequestPojo.getRoomId());
        booking.setRoom(room);

        // Logged-in user = seeker
        User seeker = jwtTokenUtil.getLoggedInUser();
        booking.setUser(seeker);

        // Default status
        booking.setStatus(bookingRequestPojo.getStatus());
        bookingRepository.save(booking);

        // ✅ Notify the room owner by email
        User owner = room.getCreatedByUser();
        if (owner != null && owner.getEmail() != null) {
            emailService.sendRoomBookingRequestEmail(
                    owner.getEmail(),
                    "New Booking Request for Your Room",
                    booking
            );
        }

        return bookingRequestPojo;
    }


    @Override
    public PagedResponse<BookingResponseProjection> getAllBookings(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Long loggedInUserId = jwtTokenUtil.getUserIdFromToken();
        boolean isSuperAdmin = jwtTokenUtil.hasRole("ROLE_SUPER_ADMIN");

        Page<BookingResponseProjection> responseProjections;

        if (isSuperAdmin) {
            // Super admin can see all bookings
            responseProjections = bookingRepository.getAllBooking(pageable);
        } else if (jwtTokenUtil.hasRole("ROLE_ADMIN")) {
            // Room admin sees bookings of their rooms
            responseProjections = bookingRepository.getBookingsByRoomOwnerId(loggedInUserId, pageable);
        } else {
            // Normal user sees only their own bookings
            responseProjections = bookingRepository.getBookingsByUserId(loggedInUserId, pageable);
        }

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

    @Override
    public void deleteBooking(Long id) {
        Booking booking = finById(id);

        Long loggedInUserId = jwtTokenUtil.getUserIdFromToken();
        boolean isAdmin = jwtTokenUtil.hasRole("ROLE_SUPER_ADMIN");

        if (!isAdmin && !booking.getUser().getId().equals(loggedInUserId)) {
            throw new UnauthorizedException("You are not allowed to delete this booking");
        }

        bookingRepository.delete(booking);
    }




    //admin side for the room booking status


    @Override
    public void updateBookingStatus(Long bookingId, BookingStatus status) throws MessagingException {

        Long loggedInUserId = jwtTokenUtil.getUserIdFromToken();
        boolean isSuperAdmin = jwtTokenUtil.hasRole("ROLE_SUPER_ADMIN");

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Only Super Admin OR the Room Owner can update status
        if (!isSuperAdmin && !booking.getRoom().getCreatedByUser().getId().equals(loggedInUserId)) {
            throw new RuntimeException("You are not authorized to update this booking");
        }

        // ✅ Get seeker email (the user who booked)
        String seekerEmail = booking.getUser().getEmail();

        // Update status
        booking.setStatus(status);
        bookingRepository.save(booking);

        // Send email notification
        emailService.sendRoomBookingConfirmationEmail(
                seekerEmail,
                "Booking Status Update - Room " + booking.getRoom().getTitle(),
                status
        );
    }













}
