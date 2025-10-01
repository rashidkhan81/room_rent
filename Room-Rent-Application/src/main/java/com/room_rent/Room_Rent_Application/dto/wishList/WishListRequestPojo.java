package com.room_rent.Room_Rent_Application.dto.wishList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WishListRequestPojo {

    private Integer userId;
    private Long roomId;

}
