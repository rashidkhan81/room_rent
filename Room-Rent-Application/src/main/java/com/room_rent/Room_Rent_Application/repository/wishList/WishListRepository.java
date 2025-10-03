package com.room_rent.Room_Rent_Application.repository.wishList;

import com.room_rent.Room_Rent_Application.dto.wishList.WishListResponseProjection;
import com.room_rent.Room_Rent_Application.model.wishList.WishList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {

    @Query("SELECT NEW com.room_rent.Room_Rent_Application.dto.wishList.WishListResponseProjection( " +
            "w.id, w.room.id, w.user.id) " +
            "FROM  WishList w")
    Page<WishListResponseProjection> getAllReviewRatingForLandingPage(Pageable pageable);

    @Query("SELECT new com.room_rent.Room_Rent_Application.dto.wishList.WishListResponseProjection(" +
            "w.id, w.room.id, w.user.id) " +
            "FROM WishList w " +
            "WHERE w.user.id = :userId")
    Page<WishListResponseProjection> getWishListByUserId(Long userId, Pageable pageable);


}
