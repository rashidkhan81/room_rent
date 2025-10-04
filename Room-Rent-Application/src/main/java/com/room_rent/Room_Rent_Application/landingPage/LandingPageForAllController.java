package com.room_rent.Room_Rent_Application.landingPage;


import com.room_rent.Room_Rent_Application.common.BaseController;
import com.room_rent.Room_Rent_Application.dto.reviewRating.ReviewRatingForLandingPage;
import com.room_rent.Room_Rent_Application.dto.room.RoomResponseProjection;
import com.room_rent.Room_Rent_Application.enums.room.RoomType;
import com.room_rent.Room_Rent_Application.paginationPageResponse.PagedResponse;
import com.room_rent.Room_Rent_Application.security.JwtUtil;
import com.room_rent.Room_Rent_Application.service.reviewRating.ReviewRatingService;
import com.room_rent.Room_Rent_Application.service.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import static com.room_rent.Room_Rent_Application.message.SuccessResponseConstant.FETCHED_LIST;
import static com.room_rent.Room_Rent_Application.message.SuccessResponseConstant.REVIEW_RATING;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public")
public class LandingPageForAllController extends BaseController {

    private final RoomService roomService;
    private final ReviewRatingService reviewRatingService;

    @GetMapping("rooms")
    public ResponseEntity<PagedResponse<RoomResponseProjection>> getAllRooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) RoomType roomType,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice
    ) {
        Pageable pageable = PageRequest.of(page, size);

        PagedResponse<RoomResponseProjection> response = roomService.getPublicRooms(
                pageable, city, district, roomType, minPrice, maxPrice
        );

        return ResponseEntity.ok(response);
    }


//    // Helper method to build standard success response
//    private <T> Object successResponse(String message, T data) {
//        return new Object() {
//            public final String message = message;
//            public final T payload = data;
//        };
//    }


    @GetMapping("/review-rating")
    public ResponseEntity<?> getReviewRatings(

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PagedResponse<ReviewRatingForLandingPage> response = reviewRatingService.getReviewRatingsForLandingPage(page, size);
        return ResponseEntity.ok(
                successResponse(
                        customMessageSource.get(FETCHED_LIST, customMessageSource.get(REVIEW_RATING)),
                        response
                )
        );
    }


}
