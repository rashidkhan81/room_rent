package com.room_rent.Room_Rent_Application.service.reviewRating;


import com.room_rent.Room_Rent_Application.dto.reviewRating.ReviewRatingRequestProjection;
import com.room_rent.Room_Rent_Application.dto.reviewRating.ReviewRatingResponseProjection;
import com.room_rent.Room_Rent_Application.model.reviewRating.ReviewRating;
import com.room_rent.Room_Rent_Application.paginationPageResponse.PagedResponse;

public interface ReviewRatingService {

    ReviewRating findById(Long id);

     ReviewRatingRequestProjection saveReviewRating(ReviewRatingRequestProjection
                                                                  projection,
                                                          Long id);


    PagedResponse<ReviewRatingResponseProjection> getReviewRatings(Long id, int page , int size);

    void deleteReviewRating(Long id);

//for landing page
    PagedResponse<ReviewRatingResponseProjection> getReviewRatingsForLandingPage( int page , int size);

}
