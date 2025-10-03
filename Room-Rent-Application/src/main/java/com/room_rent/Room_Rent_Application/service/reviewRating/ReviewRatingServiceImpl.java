package com.room_rent.Room_Rent_Application.service.reviewRating;

import com.room_rent.Room_Rent_Application.dto.reviewRating.ReviewRatingRequestProjection;
import com.room_rent.Room_Rent_Application.dto.reviewRating.ReviewRatingResponseProjection;
import com.room_rent.Room_Rent_Application.exception.NotFoundException;
import com.room_rent.Room_Rent_Application.exception.UnauthorizedException;
import com.room_rent.Room_Rent_Application.jwtTokenUtils.JwtTokenUtil;
import com.room_rent.Room_Rent_Application.message.CustomMessageSource;
import com.room_rent.Room_Rent_Application.model.reviewRating.ReviewRating;
import com.room_rent.Room_Rent_Application.paginationPageResponse.PagedResponse;
import com.room_rent.Room_Rent_Application.repository.reviewRating.ReviewRatingRepository;
import com.room_rent.Room_Rent_Application.service.AuthService;
import com.room_rent.Room_Rent_Application.service.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.room_rent.Room_Rent_Application.message.SuccessResponseConstant.*;

@Service
@RequiredArgsConstructor
public class ReviewRatingServiceImpl implements ReviewRatingService {

    private final CustomMessageSource customMessageSource;
    private final ReviewRatingRepository reviewRatingRepository;
    private final RoomService roomService;
    private final AuthService authService;
    private final JwtTokenUtil jwtTokenUtil;


    @Override
    public ReviewRating findById(Long id) {
        return reviewRatingRepository.findById(id).orElseThrow(() -> new
                NotFoundException(customMessageSource.get(NOT_FOUND, customMessageSource.get(REVIEW_RATING))));
    }

    @Override
    public ReviewRatingRequestProjection saveReviewRating(ReviewRatingRequestProjection projection, Long id) {

        ReviewRating reviewRating;

        if (Objects.nonNull(id)) {
            // Updating an existing review
            reviewRating = reviewRatingRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(
                            customMessageSource.get(NOT_FOUND, customMessageSource.get(REVIEW_RATING))
                    ));

            // Only allow update if super admin or owner
            Long loggedInUserId = jwtTokenUtil.getUserIdFromToken();
            boolean isAdmin = jwtTokenUtil.hasRole("ROLE_SUPER_ADMIN");

            if (!isAdmin && !reviewRating.getUser().getId().equals(loggedInUserId)) {
                throw new UnauthorizedException(customMessageSource.get(NOT_ALLOWED,
                        customMessageSource.get(REVIEW_RATING)));
            }
//Gets the currently logged-in user from the JWT token.
//
//Checks if the user is a super admin.
//
//If the user is not admin and is not the owner of the review → throws UnauthorizedException.
//
//✅ This ensures only the creator or a super admin can update the review.

        } else {
            // Creating a new review
            reviewRating = new ReviewRating();
            reviewRating.setUser(jwtTokenUtil.getLoggedInUser()); // set creator from JWT
        }

        // Set common fields
        reviewRating.setRating(projection.getRating());
        reviewRating.setReviewText(projection.getReviewText());
        reviewRating.setRoom(roomService.findById(projection.getRoomId()));

        reviewRatingRepository.save(reviewRating);
        return projection;
    }


    @Override
    public PagedResponse<ReviewRatingResponseProjection> getReviewRatings(Long roomId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Long loggedInUserId = jwtTokenUtil.getUserIdFromToken();
        boolean isAdmin = jwtTokenUtil.hasRole("ROLE_SUPER_ADMIN");

        Page<ReviewRatingResponseProjection> responseProjections;

        if (isAdmin) {
            // Super admin sees all reviews for this room
            responseProjections = reviewRatingRepository.getAllReviewRatingByRoomId(roomId, pageable);
        } else {
            // Normal user sees only their own reviews for this room
            responseProjections = reviewRatingRepository.getReviewRatingByRoomIdAndUserId(roomId, loggedInUserId, pageable);
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
    public PagedResponse<ReviewRatingResponseProjection> getReviewRatingsForLandingPage(int page, int size) {

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
        ReviewRating reviewRating = reviewRatingRepository.findById(id).orElseThrow(() ->
                new NotFoundException(customMessageSource.get(NOT_FOUND, customMessageSource.get(REVIEW_RATING))));
        Long loggedInUserId = jwtTokenUtil.getUserIdFromToken();
        boolean isAdmin = jwtTokenUtil.hasRole("ROLE_SUPER_ADMIN");

        if (!isAdmin && !reviewRating.getUser().getId().equals(loggedInUserId)) {
            throw new UnauthorizedException(customMessageSource.get(NOT_ALLOWED,
                    customMessageSource.get(WISH_LIST)));
        }
        reviewRatingRepository.delete(reviewRating);

    }
}
