package com.room_rent.Room_Rent_Application.service.wishList;

import com.room_rent.Room_Rent_Application.dto.wishList.WishListRequestPojo;
import com.room_rent.Room_Rent_Application.dto.wishList.WishListResponseProjection;
import com.room_rent.Room_Rent_Application.model.wishList.WishList;
import com.room_rent.Room_Rent_Application.paginationPageResponse.PagedResponse;

public interface WishListService {

    WishList findById(Long id);

    WishListRequestPojo saveWishList(WishListRequestPojo wishListRequestPojo);

    PagedResponse<WishListResponseProjection> getAllWishList(int page , int size);

    void deleteWishList(Long id);
}
