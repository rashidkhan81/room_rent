package com.room_rent.Room_Rent_Application.dto.wishList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WishListResponseProjection {
    private Long wishListId;
    private Long roomId;
    private Long userId;
}
