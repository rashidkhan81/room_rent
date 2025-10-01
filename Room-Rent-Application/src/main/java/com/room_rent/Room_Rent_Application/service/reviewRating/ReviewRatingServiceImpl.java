package com.room_rent.Room_Rent_Application.service.reviewRating;

import com.room_rent.Room_Rent_Application.dto.reviewRating.ReviewRatingRequestProjection;
import com.room_rent.Room_Rent_Application.dto.reviewRating.ReviewRatingResponseProjection;
import com.room_rent.Room_Rent_Application.exception.NotFoundException;
import com.room_rent.Room_Rent_Application.message.CustomMessageSource;
import com.room_rent.Room_Rent_Application.model.reviewRating.ReviewRating;
import com.room_rent.Room_Rent_Application.paginationPageResponse.PagedResponse;
import com.room_rent.Room_Rent_Application.repository.UserRepository;
import com.room_rent.Room_Rent_Application.repository.reviewRating.ReviewRatingRepository;
import com.room_rent.Room_Rent_Application.repository.room.RoomRepository;
import com.room_rent.Room_Rent_Application.service.AuthService;
import com.room_rent.Room_Rent_Application.service.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.room_rent.Room_Rent_Application.message.SuccessResponseConstant.NOT_FOUND;
import static com.room_rent.Room_Rent_Application.message.SuccessResponseConstant.REVIEW_RATING;

@Service
@RequiredArgsConstructor
public class ReviewRatingServiceImpl implements ReviewRatingService {

    private final CustomMessageSource customMessageSource;
    private final ReviewRatingRepository reviewRatingRepository;
    private final RoomService roomService;
    private final AuthService authService;


    @Override
    public ReviewRating findById(Long id){
        return reviewRatingRepository.findById(id).orElseThrow(()-> new
                NotFoundException(customMessageSource.get(NOT_FOUND, customMessageSource.get(REVIEW_RATING))));
    }

    @Override
    public ReviewRatingRequestProjection saveReviewRating
            (ReviewRatingRequestProjection projection, Long id) {

        ReviewRating reviewRating;
        if(Objects.nonNull(id))
            reviewRating = findById(id);
        else
            reviewRating = new ReviewRating();
        reviewRating.setRating(projection.getRating());
        reviewRating.setReviewText(projection.getReviewText());
        reviewRating.setUser(authService.findById(projection.getReviewerId()));
        reviewRating.setRoom(roomService.findById(projection.getRoomId()));
        reviewRatingRepository.save(reviewRating);
        return projection;

    }

    @Override
    public PagedResponse<ReviewRatingResponseProjection> getReviewRatings(Long id, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewRatingResponseProjection> responseProjections =
                reviewRatingRepository.getAllReviewRatingByRoomId(id, pageable);

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
    public PagedResponse<ReviewRatingResponseProjection> getReviewRatingsForLandingPage( int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewRatingResponseProjection> responseProjections =
                reviewRatingRepository.getAllReviewRatingForLandingPage(pageable);

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
    public void deleteReviewRating(Long id) {
     ReviewRating reviewRating = reviewRatingRepository.findById(id).orElseThrow(()->
             new NotFoundException( customMessageSource.get(NOT_FOUND, customMessageSource.get(REVIEW_RATING))));
         reviewRatingRepository.delete(reviewRating);

    }
}
