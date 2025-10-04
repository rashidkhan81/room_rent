package com.room_rent.Room_Rent_Application.service.reviewRating;

import com.room_rent.Room_Rent_Application.dto.file.FileResponse;
import com.room_rent.Room_Rent_Application.dto.reviewRating.ReviewRatingForLandingPage;
import com.room_rent.Room_Rent_Application.dto.reviewRating.ReviewRatingRequestProjection;
import com.room_rent.Room_Rent_Application.dto.reviewRating.ReviewRatingResponseProjection;
import com.room_rent.Room_Rent_Application.dto.room.RoomResponseProjection;
import com.room_rent.Room_Rent_Application.exception.NotFoundException;
import com.room_rent.Room_Rent_Application.exception.UnauthorizedException;
import com.room_rent.Room_Rent_Application.jwtTokenUtils.JwtTokenUtil;
import com.room_rent.Room_Rent_Application.message.CustomMessageSource;
import com.room_rent.Room_Rent_Application.model.reviewRating.ReviewRating;
import com.room_rent.Room_Rent_Application.model.room.Room;
import com.room_rent.Room_Rent_Application.paginationPageResponse.PagedResponse;
import com.room_rent.Room_Rent_Application.repository.reviewRating.ReviewRatingRepository;
import com.room_rent.Room_Rent_Application.service.AuthService;
import com.room_rent.Room_Rent_Application.service.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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

        Page<ReviewRating> reviewsPage;

        if (isAdmin) {
            // Super admin sees all reviews for this room
            reviewsPage = reviewRatingRepository.findByRoomId(roomId, pageable);
        } else {
            // Normal user sees only their own reviews for this room
            reviewsPage = reviewRatingRepository.findByRoomIdAndUserId(roomId, loggedInUserId, pageable);
        }

        Double averageRating = reviewRatingRepository.findAverageRatingByRoomId(roomId);
        if (averageRating != null) {
            averageRating = Math.round(averageRating * 10.0) / 10.0;
        }
        // map to DTO with room + files
        Double finalAverageRating = averageRating;
        List<ReviewRatingResponseProjection> content = reviewsPage.stream().map(review -> {
            Room room = review.getRoom();

            // map room images
            List<FileResponse> images = room.getImages().stream()
                    .map(img -> new FileResponse(
                            img.getId(),
                            img.getOriginalName(),
                            img.getUrl(),
                            img.getContentType(),
                            img.getSize(),
                            img.getUploadedAt(),
                            room.getCreatedByUser().getId()
                    )).toList();

            // room DTO
            RoomResponseProjection roomProjection = new RoomResponseProjection(
                    room.getId(),
                    room.getTitle(),
                    room.getDescription(),
                    room.getPrice(),
                    room.getContact(),
                    room.getAddress(),
                    room.getRoomType(),
                    images,
                    room.getUploadedAt()
            );

            // review DTO
            return new ReviewRatingResponseProjection(
                    review.getId(),
                    review.getRating(),
                    review.getReviewText(),
                    roomProjection,               // ✅ full room details here
                    review.getUser().getId(),
                    finalAverageRating
            );
        }).toList();

        // page info
        PagedResponse.PageInfo pageInfo = new PagedResponse.PageInfo(
                reviewsPage.getNumber(),
                reviewsPage.getSize(),
                reviewsPage.getTotalElements(),
                reviewsPage.getTotalPages(),
                reviewsPage.isLast()
        );

        return new PagedResponse<>(content, pageInfo);
    }


    @Override
    public PagedResponse<ReviewRatingForLandingPage> getReviewRatingsForLandingPage(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewRating> reviewsPage = reviewRatingRepository.findAll(pageable);

        List<ReviewRatingForLandingPage> content = reviewsPage.stream().map(review -> {
            Room room = review.getRoom();
            // Long createdById = room.getCreatedByUser().getId(); // ✅ This is a Long

            Double averageRating = reviewRatingRepository.findAverageRatingByRoomId(room.getId());
            if (averageRating != null) {
                averageRating = Math.round(averageRating * 10.0) / 10.0;
            }
            List<FileResponse> images = room.getImages().stream()
                    .map(img -> new FileResponse(
                            img.getId(),
                            img.getOriginalName(),
                            img.getUrl(),
                            img.getContentType(),
                            img.getSize(),
                            img.getUploadedAt(),
                            room.getCreatedByUser().getId()
                    )).toList();

            RoomResponseProjection roomProjection = new RoomResponseProjection(
                    room.getId(),
                    room.getTitle(),
                    room.getDescription(),
                    room.getPrice(),
                    room.getContact(),
                    room.getAddress(),
                    room.getRoomType(),
                    images,
                    room.getUploadedAt()
            );

            return new ReviewRatingForLandingPage(
                    review.getId(),
                    review.getRating(),
                    review.getReviewText(),
                    roomProjection,
                    review.getUser().getId(),
                    averageRating
            );
        }).toList();

        PagedResponse.PageInfo pageInfo = new PagedResponse.PageInfo(
                reviewsPage.getNumber(),
                reviewsPage.getSize(),
                reviewsPage.getTotalElements(),
                reviewsPage.getTotalPages(),
                reviewsPage.isLast()
        );

        return new PagedResponse<>(content, pageInfo);
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
