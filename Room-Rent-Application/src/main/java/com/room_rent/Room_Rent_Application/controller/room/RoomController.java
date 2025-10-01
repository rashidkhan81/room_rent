package com.room_rent.Room_Rent_Application.controller.room;

import com.room_rent.Room_Rent_Application.dto.room.RoomRequestProjection;
import com.room_rent.Room_Rent_Application.dto.room.RoomResponseProjection;
import com.room_rent.Room_Rent_Application.enums.room.RoomType;
import com.room_rent.Room_Rent_Application.model.room.Room;
import com.room_rent.Room_Rent_Application.paginationPageResponse.PagedResponse;
import com.room_rent.Room_Rent_Application.service.room.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@Valid @RequestBody RoomRequestProjection roomPojo,
                                           @PathVariable Long id) {
        return ResponseEntity.ok(roomService.saveRoom(roomPojo,id));
    }


    @PostMapping
    public ResponseEntity<Room> createRoom(@Valid @RequestBody RoomRequestProjection roomPojo) {
        return ResponseEntity.ok(roomService.saveRoom(roomPojo,null));
    }




    @GetMapping("/{id}")
    public ResponseEntity<RoomResponseProjection> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }


    @DeleteMapping("/{id}")
    public void deleteRoomById(@PathVariable Long id) {
        roomService.deleteRoom(id);

    }

}
