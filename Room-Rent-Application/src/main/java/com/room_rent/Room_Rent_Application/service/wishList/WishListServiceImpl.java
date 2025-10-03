package com.room_rent.Room_Rent_Application.service.wishList;

import com.room_rent.Room_Rent_Application.dto.wishList.WishListRequestPojo;
import com.room_rent.Room_Rent_Application.dto.wishList.WishListResponseProjection;
import com.room_rent.Room_Rent_Application.exception.NotFoundException;
import com.room_rent.Room_Rent_Application.exception.UnauthorizedException;
import com.room_rent.Room_Rent_Application.jwtTokenUtils.JwtTokenUtil;
import com.room_rent.Room_Rent_Application.message.CustomMessageSource;
import com.room_rent.Room_Rent_Application.model.User;
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

import static com.room_rent.Room_Rent_Application.message.SuccessResponseConstant.*;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;
    private final CustomMessageSource customMessageSource;
    private final RoomService roomService;
    private final AuthService authService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public WishList findById(Long id) {
        return wishListRepository.findById(id).orElseThrow(() ->
                new NotFoundException(customMessageSource.get(NOT_FOUND, customMessageSource.get(WISH_LIST))));
    }

    @Override
    public WishListRequestPojo saveWishList(WishListRequestPojo wishListRequestPojo) {

        // Get the logged-in user from JWT
        User loggedInUser = jwtTokenUtil.getLoggedInUser();

        // Optional: if you want to allow only super admin to add for others
        // boolean isAdmin = jwtTokenUtil.hasRole("ROLE_SUPER_ADMIN");
        // if (!isAdmin && !wishListRequestPojo.getUserId().equals(loggedInUser.getId())) {
        //     throw new UnauthorizedException("You cannot create wishlist for other users");
        // }

        WishList wishList = new WishList();
        wishList.setRoom(roomService.findById(wishListRequestPojo.getRoomId()));
        wishList.setUser(loggedInUser); // always assign the logged-in user

        wishListRepository.save(wishList);
        return wishListRequestPojo;
    }


    @Override
    public PagedResponse<WishListResponseProjection> getAllWishList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Long loggedInUserId = jwtTokenUtil.getUserIdFromToken();
        boolean isAdmin = jwtTokenUtil.hasRole("ROLE_SUPER_ADMIN");

        Page<WishListResponseProjection> responseProjections;

        if (isAdmin) {
            // Super admin can see all wishlists
            responseProjections = wishListRepository.getAllReviewRatingForLandingPage(pageable);
        } else {
            // Normal user sees only their own wishlist
            responseProjections = wishListRepository.getWishListByUserId(loggedInUserId, pageable);
        }

        PagedResponse.PageInfo pageInfo = new PagedResponse.PageInfo(
                responseProjections.getNumber(),
                responseProjections.getSize(),
                responseProjections.getTotalElements(),
                responseProjections.getTotalPages(),
                responseProjections.isLast()
        );

        return new PagedResponse<>(
                responseProjections.getContent(),
                pageInfo
        );
    }



    @Override
    public void deleteWishList(Long id) {
        // Fetch the wishlist entry
        WishList wishList = findById(id);

        Long loggedInUserId = jwtTokenUtil.getUserIdFromToken();
        boolean isAdmin = jwtTokenUtil.hasRole("ROLE_SUPER_ADMIN");

        // Only allow deletion if super admin or the owner of the wishlist
        if (!isAdmin && !wishList.getUser().getId().equals(loggedInUserId)) {
            throw new UnauthorizedException(customMessageSource.get(NOT_ALLOWED,
                                                            customMessageSource.get(WISH_LIST)));
        }

        wishListRepository.delete(wishList);
    }


}
