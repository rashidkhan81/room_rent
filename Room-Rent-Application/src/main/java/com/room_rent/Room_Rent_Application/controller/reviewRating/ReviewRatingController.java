package com.room_rent.Room_Rent_Application.controller.reviewRating;

import com.room_rent.Room_Rent_Application.common.BaseController;
import com.room_rent.Room_Rent_Application.dto.reviewRating.ReviewRatingRequestProjection;
import com.room_rent.Room_Rent_Application.dto.reviewRating.ReviewRatingResponseProjection;
import com.room_rent.Room_Rent_Application.message.CustomMessageSource;
import com.room_rent.Room_Rent_Application.paginationPageResponse.PagedResponse;
import com.room_rent.Room_Rent_Application.service.reviewRating.ReviewRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.room_rent.Room_Rent_Application.message.SuccessResponseConstant.*;
import static com.room_rent.Room_Rent_Application.message.SuccessResponseConstant.REVIEW_RATING;

@RestController
@RequestMapping("/api/review-rating")
@RequiredArgsConstructor
public class ReviewRatingController extends BaseController {

    private final ReviewRatingService reviewRatingService;
    private final CustomMessageSource customMessageSource;

    // Create or update review rating
    @PostMapping("/review-rating")
    public ResponseEntity<?> saveReviewRating(
            @RequestBody ReviewRatingRequestProjection projection
    ) {
        ReviewRatingRequestProjection savedProjection = reviewRatingService.saveReviewRating(projection,null);
        return ResponseEntity.ok(
                successResponse(
                        customMessageSource.get(SUCCESS_SAVE, customMessageSource.get(REVIEW_RATING)),
                        savedProjection
                )
        );
    }

    // Get paginated review ratings for a room
    @GetMapping("/{roomId}")
    public ResponseEntity<?> getReviewRatings(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PagedResponse<ReviewRatingResponseProjection> response = reviewRatingService.getReviewRatings(roomId, page, size);
        return ResponseEntity.ok(
                successResponse(
                        customMessageSource.get(FETCHED_LIST, customMessageSource.get(REVIEW_RATING)),
                        response
                )
        );
    }

    // Delete review rating
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReviewRating(@PathVariable Long id) {
        reviewRatingService.deleteReviewRating(id);
        return ResponseEntity.ok(
                successResponse(
                        customMessageSource.get(SUCCESS_DELETE, customMessageSource.get(REVIEW_RATING)),
                        null
                )
        );
    }
}
