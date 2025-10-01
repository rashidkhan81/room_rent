package com.room_rent.Room_Rent_Application.controller.whishListController;

import com.room_rent.Room_Rent_Application.common.BaseController;
import com.room_rent.Room_Rent_Application.message.CustomMessageSource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/room/wish-list")
@RequiredArgsConstructor
public class WishListController extends BaseController {

    private CustomMessageSource customMessageSource;
}
