package com.room_rent.Room_Rent_Application.service.wishList;

import com.room_rent.Room_Rent_Application.dto.wishList.WishListRequestPojo;
import com.room_rent.Room_Rent_Application.dto.wishList.WishListResponseProjection;
import com.room_rent.Room_Rent_Application.exception.NotFoundException;
import com.room_rent.Room_Rent_Application.message.CustomMessageSource;
import com.room_rent.Room_Rent_Application.model.wishList.WishList;
import com.room_rent.Room_Rent_Application.paginationPageResponse.PagedResponse;
import com.room_rent.Room_Rent_Application.repository.wishList.WishListRepository;
import com.room_rent.Room_Rent_Application.service.AuthService;
import com.room_rent.Room_Rent_Application.service.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.room_rent.Room_Rent_Application.message.SuccessResponseConstant.NOT_FOUND;
import static com.room_rent.Room_Rent_Application.message.SuccessResponseConstant.WISH_LIST;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;
    private final CustomMessageSource customMessageSource;
    private final RoomService roomService;
    private final AuthService authService;

    @Override
    public WishList findById(Long id) {
        return wishListRepository.findById(id).orElseThrow(() ->
                new NotFoundException(customMessageSource.get(NOT_FOUND, customMessageSource.get(WISH_LIST))));
    }

    @Override
    public WishListRequestPojo saveWishList(WishListRequestPojo wishListRequestPojo) {

        WishList wishList = new WishList();
        wishList.setRoom(roomService.findById(wishListRequestPojo.getRoomId()));
        wishList.setUser(authService.findById(wishListRequestPojo.getUserId()));

        wishListRepository.save(wishList);
        return wishListRequestPojo;


    }

    @Override
    public PagedResponse<WishListResponseProjection> getAllWishList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WishListResponseProjection> responseProjections =
                wishListRepository.getAllReviewRatingForLandingPage(pageable);

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
    public void deleteWishList(Long id) {

        WishList wishList;
        wishList = findById(id);
        wishListRepository.delete(wishList);


    }
}
