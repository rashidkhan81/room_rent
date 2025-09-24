package com.room_rent.Room_Rent_Application.exception;

import org.hibernate.service.spi.ServiceException;

public class NotFoundException extends ServiceException {
    public NotFoundException(String msg) {
        super(msg);
    }
}
