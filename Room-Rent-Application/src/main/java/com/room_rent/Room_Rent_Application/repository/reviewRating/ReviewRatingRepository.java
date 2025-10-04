package com.room_rent.Room_Rent_Application.repository.reviewRating;

import com.room_rent.Room_Rent_Application.dto.reviewRating.ReviewRatingResponseProjection;
import com.room_rent.Room_Rent_Application.model.reviewRating.ReviewRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRatingRepository extends JpaRepository<ReviewRating, Long> {


//    @Query("SELECT NEW com.room_rent.Room_Rent_Application.dto.reviewRating.ReviewRatingResponseProjection( " +
//            "r.id, r.rating, r.reviewText, r.room.id, r.user.id) " +
//            "FROM ReviewRating r " +
//            "WHERE  r.room.id = :roomId")
//    Page<ReviewRatingResponseProjection> getAllReviewRatingByRoomId(
//            @Param("roomId") Long roomId, Pageable pageable);
//
//
//    @Query("SELECT NEW com.room_rent.Room_Rent_Application.dto.reviewRating.ReviewRatingResponseProjection( " +
//            "r.id, r.rating, r.reviewText, r.room.id, r.user.id) " +
//            "FROM ReviewRating r " +
//            "WHERE r.room.id = :roomId AND r.user.id = :userId")
//    Page<ReviewRatingResponseProjection> getReviewRatingByRoomIdAndUserId(
//            @Param("roomId") Long roomId,
//            @Param("userId") Long userId,
//            Pageable pageable);

//
//
//    @Query("SELECT NEW com.room_rent.Room_Rent_Application.dto.reviewRating.ReviewRatingResponseProjection( " +
//            "r.id, r.rating, r.reviewText, " +
//            "NEW com.room_rent.Room_Rent_Application.dto.room.RoomResponseProjection(" +
//            "rm.id, rm.title, rm.description, rm.price, rm.contact, rm.address, rm.roomType, rm.images, rm.uploadedAt), " +
//            "r.user.id) " +
//            "FROM ReviewRating r " +
//            "JOIN r.room rm")
//    Page<ReviewRatingResponseProjection> getAllReviewRatingForLandingPage(Pageable pageable);

    Page<ReviewRating> findByRoomId(Long roomId, Pageable pageable);

    Page<ReviewRating> findByRoomIdAndUserId(Long roomId, Long userId, Pageable pageable);
}
