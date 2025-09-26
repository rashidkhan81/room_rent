package com.room_rent.Room_Rent_Application.controller.common;

import com.room_rent.Room_Rent_Application.Global.GlobalApiResponse;
import com.room_rent.Room_Rent_Application.message.CustomMessageSource;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseController {

    @Autowired
    protected CustomMessageSource customMessageSource;




}
