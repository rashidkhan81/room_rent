package com.room_rent.Room_Rent_Application.service.room;

import com.room_rent.Room_Rent_Application.dto.room.RoomFilterRequest;
import com.room_rent.Room_Rent_Application.dto.room.RoomRequest;
import com.room_rent.Room_Rent_Application.dto.room.RoomResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RoomService {
    RoomResponse createRoom(RoomRequest request, MultipartFile[] mediaFiles, String adminEmail) throws IOException;
    RoomResponse updateRoom(Long id, RoomRequest request, MultipartFile[] mediaFiles, String adminEmail) throws IOException;
    void deleteRoom(Long id, String adminEmail);
    List<RoomResponse> getRoomsByAdmin(String adminEmail);
    List<RoomResponse> getAllRooms(RoomFilterRequest filter); // Super Admin only
}
