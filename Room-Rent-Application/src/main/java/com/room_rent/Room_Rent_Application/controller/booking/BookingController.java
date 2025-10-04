package com.room_rent.Room_Rent_Application.controller.booking;

import com.room_rent.Room_Rent_Application.common.BaseController;
import com.room_rent.Room_Rent_Application.dto.booking.BookingRequestPojo;
import com.room_rent.Room_Rent_Application.dto.booking.BookingResponseProjection;
import com.room_rent.Room_Rent_Application.enums.room.booking.BookingStatus;
import com.room_rent.Room_Rent_Application.message.CustomMessageSource;
import com.room_rent.Room_Rent_Application.paginationPageResponse.PagedResponse;
import com.room_rent.Room_Rent_Application.service.booking.BookingService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.room_rent.Room_Rent_Application.message.SuccessResponseConstant.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/booking")
public class BookingController extends BaseController {

    private final BookingService bookingService;
    private final CustomMessageSource customMessageSource;


    @PostMapping("/booking")
    public ResponseEntity<?> saveReviewRating(
            @RequestBody BookingRequestPojo projection
    ) throws MessagingException {
        BookingRequestPojo savedProjection = bookingService.save(projection);
        return ResponseEntity.ok(
                successResponse(
                        customMessageSource.get(SUCCESS_SAVE, customMessageSource.get(REVIEW_RATING)),
                        savedProjection
                )
        );
    }

    @GetMapping("/booking-get")
    public ResponseEntity<?> getReviewRatings(

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PagedResponse<BookingResponseProjection> response = bookingService.getAllBookings(page, size);
        return ResponseEntity.ok(
                successResponse(
                        customMessageSource.get(FETCHED_LIST, customMessageSource.get(BOOKING)),
                        response
                )
        );
    }


    @PatchMapping("/update-status/{bookingId}")
    public ResponseEntity<?> updateBookingStatus(
            @PathVariable Long bookingId,
            @RequestParam BookingStatus status
    ) throws MessagingException {
        bookingService.updateBookingStatus(bookingId, status);
        return ResponseEntity.ok(
                successResponse(
                        customMessageSource.get(SUCCESS_UPDATE, customMessageSource.get(BOOKING)),
                        null
                )
        );
    }


}
