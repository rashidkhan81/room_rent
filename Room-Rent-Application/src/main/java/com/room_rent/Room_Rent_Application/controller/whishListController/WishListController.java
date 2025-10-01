package com.room_rent.Room_Rent_Application.controller.whishListController;

import com.room_rent.Room_Rent_Application.common.BaseController;
import com.room_rent.Room_Rent_Application.dto.wishList.WishListRequestPojo;
import com.room_rent.Room_Rent_Application.dto.wishList.WishListResponseProjection;
import com.room_rent.Room_Rent_Application.message.CustomMessageSource;
import com.room_rent.Room_Rent_Application.paginationPageResponse.PagedResponse;
import com.room_rent.Room_Rent_Application.service.wishList.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.room_rent.Room_Rent_Application.message.SuccessResponseConstant.*;

@RestController
@RequestMapping("/api/room/wish-list")
@RequiredArgsConstructor
public class WishListController extends BaseController {

    private final WishListService wishListService;
    private final CustomMessageSource customMessageSource;


    // Create or update review rating
    @PostMapping("/review-rating")
    public ResponseEntity<?> saveReviewRating(
            @RequestBody WishListRequestPojo projection
    ) {
        WishListRequestPojo savedProjection = wishListService.saveWishList(projection);
        return ResponseEntity.ok(
                successResponse(
                        customMessageSource.get(SUCCESS_SAVE, customMessageSource.get(WISH_LIST)),
                        savedProjection
                )
        );
    }


    @GetMapping("/wish-list")
    public ResponseEntity<?> getReviewRatings(

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PagedResponse<WishListResponseProjection> response = wishListService.getAllWishList(page, size);
        return ResponseEntity.ok(
                successResponse(
                        customMessageSource.get(FETCHED_LIST, customMessageSource.get(WISH_LIST)),
                        response
                )
        );
    }

    @DeleteMapping("/remove-wish-list/{id}")
    public ResponseEntity<?> removeWishList(
            @PathVariable Long id
    ) {
        wishListService.deleteWishList(id);
        return ResponseEntity.ok(
                successResponse(
                        customMessageSource.get(SUCCESS_DELETE, customMessageSource.get(WISH_LIST)),
                        null
                )
        );
    }


}
