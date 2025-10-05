package com.room_rent.Room_Rent_Application.service.superAdminDashboard;

import com.room_rent.Room_Rent_Application.repository.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SuperAdminDashboardServiceImpl {

    private final RoomRepository roomRepository;

    public Long getTotalRoom(){
       return roomRepository.countTotalRooms();
    }
}
