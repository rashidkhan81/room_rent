package com.room_rent.Room_Rent_Application.jwtTokenUtils;

import com.room_rent.Room_Rent_Application.RoomRentApplication;
import com.room_rent.Room_Rent_Application.exception.UnauthorizedException;
import com.room_rent.Room_Rent_Application.message.CustomMessageSource;
import com.room_rent.Room_Rent_Application.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.room_rent.Room_Rent_Application.message.SuccessResponseConstant.*;

@Component
@RequiredArgsConstructor
public class AccessControlService {
    private final JwtTokenUtil jwtTokenUtil;
    private CustomMessageSource customMessageSource;

    public void checkOwnershipOrRole(Long resourceOwnerId, String role) {
        Long userId = jwtTokenUtil.getUserIdFromToken();
        if (!jwtTokenUtil.hasRole(role) && !resourceOwnerId.equals(userId)) {
            throw new UnauthorizedException(customMessageSource.get(NOT_FOUND, customMessageSource.get(ROLE)));
        }
    }


}
