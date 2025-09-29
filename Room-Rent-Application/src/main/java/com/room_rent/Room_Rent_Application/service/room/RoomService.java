package com.room_rent.Room_Rent_Application.service.room;

import com.room_rent.Room_Rent_Application.dto.room.RoomRequestProjection;
import com.room_rent.Room_Rent_Application.dto.room.RoomResponseProjection;
import com.room_rent.Room_Rent_Application.enums.room.RoomType;
import com.room_rent.Room_Rent_Application.model.room.Room;
import com.room_rent.Room_Rent_Application.paginationPageResponse.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface RoomService {
    Room saveRoom(RoomRequestProjection roomPojo, Long id);
//    PagedResponse<RoomResponseProjection> getRooms(Pageable pageable, String searchKeyword);
PagedResponse<RoomResponseProjection> getRooms(
        Pageable pageable,
        String city,
        String district,
        RoomType roomType,
        BigDecimal minPrice,
        BigDecimal maxPrice
);

    RoomResponseProjection getRoomById(Long roomId);

    public void deleteRoom (Long id);
}